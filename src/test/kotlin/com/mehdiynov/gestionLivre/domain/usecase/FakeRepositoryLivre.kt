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
}