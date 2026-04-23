package com.mehdiynov.gestionLivre.infrastructure.driven.adapter

import com.mehdiynov.gestionLivre.domain.model.Livre
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables

@SpringBootTest
class LivreDAOIT : FunSpec() {

    @Autowired
    lateinit var livreDAO: LivreDAO

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    init {
        extension(SpringExtension)

        beforeTest {
            jdbcTemplate.update("DELETE FROM livre", MapSqlParameterSource())
        }

        test("save et findAll retournent les livres avec disponibilite") {
            livreDAO.save(Livre("Livre mehdi", "Lazaar", false))
            livreDAO.save(Livre("DDD", "Eric Evans", true))

            val livres = livreDAO.findAll()

            livres shouldHaveSize 2

            val livreMehdi = livres.find { it.titre == "Livre mehdi" && it.auteur == "Lazaar" }
            val ddd = livres.find { it.titre == "DDD" && it.auteur == "Eric Evans" }

            livreMehdi shouldBe Livre("Livre mehdi", "Lazaar", false)
            ddd shouldBe Livre("DDD", "Eric Evans", true)
        }

        test("findByTitreAndAuteur retourne un livre") {
            livreDAO.save(Livre("Livre mehdi", "Lazaar", false))

            val livre = livreDAO.findByTitreAndAuteur("Livre mehdi", "Lazaar")

            livre shouldBe Livre("Livre mehdi", "Lazaar", false)
        }

        test("reserve met le livre a reserve=true") {
            livreDAO.save(Livre("Livre mehdi", "Lazaar", false))

            livreDAO.reserve("Livre mehdi", "Lazaar")

            val livre = livreDAO.findByTitreAndAuteur("Livre mehdi", "Lazaar")

            livre!!.estReserve shouldBe true
        }
    }

    companion object {
        private val container = PostgreSQLContainer<Nothing>("postgres:13-alpine")

        init {
            Startables.deepStart(container).join()
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { container.jdbcUrl }
            registry.add("spring.datasource.username") { container.username }
            registry.add("spring.datasource.password") { container.password }
        }
    }
}