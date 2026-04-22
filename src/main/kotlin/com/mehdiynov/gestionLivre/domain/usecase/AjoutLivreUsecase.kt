package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class AjoutLivreUsecase(private val repository: LivreRepository) {

    fun execute(titre: String, auteur: String) {
        if (titre.isBlank() || auteur.isBlank()) {
            throw IllegalArgumentException("Titre et auteur ne doivent pas être vides")
        }

        val livre = Livre(titre, auteur)
        repository.save(livre)
    }
}