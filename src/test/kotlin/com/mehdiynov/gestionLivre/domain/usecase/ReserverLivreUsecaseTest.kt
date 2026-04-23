package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ReserverLivreUsecaseTest : FunSpec({

    test("reserve un livre disponible") {
        val repository = FakeLivreRepository(
            mutableListOf(Livre("livre mehdi", "Lazaar", false))
        )
        val usecase = ReserverLivreUsecase(repository)

        usecase.execute("livre mehdi", "Lazaar")

        repository.findByTitreAndAuteur("livre mehdi", "Lazaar")!!.estReserve shouldBe true
    }

    test("refuse un livre deja reserve") {
        val repository = FakeLivreRepository(
            mutableListOf(Livre("livre mehdi", "Lazaar", true))
        )
        val usecase = ReserverLivreUsecase(repository)

        val exception = shouldThrow<IllegalArgumentException> {
            usecase.execute("livre mehdi", "Lazaar")
        }

        exception.message shouldBe "Le livre est déjà réservé"
    }

    test("retourne une erreur si le livre est introuvable") {
        val repository = FakeLivreRepository(mutableListOf())
        val usecase = ReserverLivreUsecase(repository)

        val exception = shouldThrow<IllegalArgumentException> {
            usecase.execute("Inconnu", "Auteur inconnu")
        }

        exception.message shouldBe "Livre introuvable"
    }
})

private class FakeLivreRepository(
    private val livres: MutableList<Livre>
) : LivreRepository {

    override fun save(livre: Livre) {
        livres.add(livre)
    }

    override fun findAll(): List<Livre> {
        return livres.toList()
    }

    override fun findByTitreAndAuteur(titre: String, auteur: String): Livre? {
        return livres.firstOrNull { it.titre == titre && it.auteur == auteur }
    }

    override fun reserve(titre: String, auteur: String) {
        val index = livres.indexOfFirst { it.titre == titre && it.auteur == auteur }
        if (index >= 0) {
            val livre = livres[index]
            livres[index] = livre.copy(estReserve = true)
        }
    }
}
