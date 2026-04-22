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

    override fun findAll(): List<Livre> {
        return namedParameterJdbcTemplate.query(
            "SELECT titre, auteur FROM livre",
            MapSqlParameterSource()
        ) { rs, _ ->
            Livre(
                titre = rs.getString("titre"),
                auteur = rs.getString("auteur")
            )
        }
    }

    override fun save(livre: Livre) {
        namedParameterJdbcTemplate.update(
            "INSERT INTO livre (titre, auteur) VALUES (:titre, :auteur)",
            mapOf(
                "titre" to livre.titre,
                "auteur" to livre.auteur
            )
        )
    }
}