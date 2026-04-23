package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class ListeLivreUsecase(private val repository: LivreRepository) {
    fun execute(): List<Livre>{
        return repository.findAll().sortedBy { it.titre }
    }
}
