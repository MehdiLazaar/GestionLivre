package com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto

import jakarta.validation.constraints.NotBlank

data class CreateBookDTO(
    @field:NotBlank(message = "Le titre ne doit pas être vide")
    val titre: String,

    @field:NotBlank(message = "L'auteur ne doit pas être vide")
    val auteur: String
)
