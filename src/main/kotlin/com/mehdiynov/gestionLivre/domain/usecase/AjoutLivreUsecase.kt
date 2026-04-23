package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class AjoutLivreUsecase(
    private val livreRepository: LivreRepository
) {
    fun execute(titre: String, auteur: String) {
        require(titre.isNotBlank()) { "Le titre ne doit pas être vide" }
        require(auteur.isNotBlank()) { "L'auteur ne doit pas être vide" }

        livreRepository.save(Livre(titre, auteur))
    }
}
