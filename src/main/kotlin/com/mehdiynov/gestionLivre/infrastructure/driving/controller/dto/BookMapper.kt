package com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto

import com.mehdiynov.gestionLivre.domain.model.Livre

fun Livre.toResponseDto(): BookResponseDTO {
    return BookResponseDTO(
        titre = this.titre,
        auteur = this.auteur,
        disponible = !this.estReserve
    )
}
