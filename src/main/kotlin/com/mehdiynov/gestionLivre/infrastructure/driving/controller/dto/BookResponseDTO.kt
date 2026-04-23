package com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto

data class BookResponseDTO(
    val titre: String,
    val auteur: String,
    val disponible: Boolean
)
