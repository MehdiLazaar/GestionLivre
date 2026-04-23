package com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto

import com.mehdiynov.gestionLivre.domain.model.Livre
import jakarta.validation.constraints.NotBlank

data class BookDTO(
    @field:NotBlank(message = "Le titre ne doit pas être vide")
    val titre: String,

    @field:NotBlank(message = "L'auteur ne doit pas être vide")
    val auteur: String
)

fun BookDTO.toDomain(): Livre {
    return Livre(
        titre = this.titre,
        auteur = this.auteur
    )
}

fun Livre.toDto(): BookDTO {
    return BookDTO(
        titre = this.titre,
        auteur = this.auteur
    )
}
