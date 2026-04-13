package com.mehdiynov.gestionLivre.domain.port

import com.mehdiynov.gestionLivre.domain.model.Livre

interface LivreRepository {
    fun save(livre: Livre)
    fun findAll(): List<Livre>
}