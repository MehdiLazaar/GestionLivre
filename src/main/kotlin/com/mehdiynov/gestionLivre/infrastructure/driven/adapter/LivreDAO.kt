package com.mehdiynov.gestionLivre.infrastructure.driven.adapter
import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LivreDAO(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : LivreRepository {

    override fun save(livre: Livre) {
        namedParameterJdbcTemplate.update(
            """
            INSERT INTO livre (titre, auteur, reserve)
            VALUES (:titre, :auteur, :reserve)
            """.trimIndent(),
            mapOf(
                "titre" to livre.titre,
                "auteur" to livre.auteur,
                "reserve" to livre.estReserve
            )
        )
    }

    override fun findAll(): List<Livre> {
        return namedParameterJdbcTemplate.query(
            """
            SELECT titre, auteur, reserve
            FROM livre
            """.trimIndent(),
            MapSqlParameterSource()
        ) { rs, _ ->
            Livre(
                titre = rs.getString("titre"),
                auteur = rs.getString("auteur"),
                estReserve = rs.getBoolean("reserve")
            )
        }
    }

    override fun findByTitreAndAuteur(titre: String, auteur: String): Livre? {
        return namedParameterJdbcTemplate.query(
            """
            SELECT titre, auteur, reserve
            FROM livre
            WHERE titre = :titre AND auteur = :auteur
            LIMIT 1
            """.trimIndent(),
            mapOf(
                "titre" to titre,
                "auteur" to auteur
            )
        ) { rs, _ ->
            Livre(
                titre = rs.getString("titre"),
                auteur = rs.getString("auteur"),
                estReserve = rs.getBoolean("reserve")
            )
        }.firstOrNull()
    }

    override fun reserve(titre: String, auteur: String) {
        namedParameterJdbcTemplate.update(
            """
            UPDATE livre
            SET reserve = true
            WHERE titre = :titre AND auteur = :auteur
            """.trimIndent(),
            mapOf(
                "titre" to titre,
                "auteur" to auteur
            )
        )
    }
}
