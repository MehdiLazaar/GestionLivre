package com.mehdiynov.gestionLivre.infrastructure.driving.controller

import com.mehdiynov.gestionLivre.domain.usecase.AjoutLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ListeLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ReserverLivreUsecase
import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.BookResponseDTO
import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.CreateBookDTO
import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.ReserveBookDTO
import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.toResponseDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    private val ajoutLivreUsecase: AjoutLivreUsecase,
    private val listeLivreUsecase: ListeLivreUsecase,
    private val reserverLivreUsecase: ReserverLivreUsecase
) {

    @GetMapping
    fun getAllBooks(): List<BookResponseDTO> {
        return listeLivreUsecase.execute().map { it.toResponseDto() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@Valid @RequestBody dto: CreateBookDTO) {
        ajoutLivreUsecase.execute(dto.titre, dto.auteur)
    }

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reserveBook(@Valid @RequestBody dto: ReserveBookDTO) {
        reserverLivreUsecase.execute(dto.titre, dto.auteur)
    }
}
