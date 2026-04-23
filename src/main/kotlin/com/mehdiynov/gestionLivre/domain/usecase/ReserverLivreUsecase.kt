package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class ReserverLivreUsecase(
    private val livreRepository: LivreRepository
) {
    fun execute(titre: String, auteur: String) {
        require(titre.isNotBlank()) { "Le titre ne doit pas être vide" }
        require(auteur.isNotBlank()) { "L'auteur ne doit pas être vide" }

        val livre = livreRepository.findByTitreAndAuteur(titre, auteur)
            ?: throw IllegalArgumentException("Livre introuvable")

        require(!livre.estReserve) { "Le livre est déjà réservé" }

        livreRepository.reserve(titre, auteur)
    }
}
