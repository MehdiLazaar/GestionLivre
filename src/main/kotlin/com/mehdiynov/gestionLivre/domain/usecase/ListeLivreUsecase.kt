package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class ListeLivreUsecase(
    private val livreRepository: LivreRepository
) {
    fun execute(): List<Livre> {
        return livreRepository.findAll()
            .sortedBy { it.titre.lowercase() }
    }
}
