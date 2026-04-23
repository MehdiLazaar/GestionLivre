package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class FakeRepositoryLivre : LivreRepository {

    private val livres = mutableListOf<Livre>()

    override fun save(livre: Livre) {
        livres.add(livre)
    }

    override fun findAll(): List<Livre> {
        return livres
    }

    override fun findByTitreAndAuteur(
        titre: String,
        auteur: String
    ): Livre? {
        return livres.find { it.titre == titre && it.auteur == auteur }
    }

    override fun reserve(titre: String, auteur: String) {
        val livre = findByTitreAndAuteur(titre, auteur)
        if (livre != null) {
            livre.estReserve = true
        }
    }
}
