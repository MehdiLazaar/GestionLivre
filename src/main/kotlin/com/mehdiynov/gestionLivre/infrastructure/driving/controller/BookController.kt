package com.mehdiynov.gestionLivre.infrastructure.driving.controller

import com.mehdiynov.gestionLivre.domain.usecase.AjoutLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ListeLivreUsecase
import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.BookDTO
import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.toDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    private val ajoutLivreUsecase: AjoutLivreUsecase,
    private val listeLivreUsecase: ListeLivreUsecase
) {

    @GetMapping
    fun getAllBooks(): List<BookDTO> {
        return listeLivreUsecase.execute().map { it.toDto() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@Valid @RequestBody dto: BookDTO) {
        ajoutLivreUsecase.execute(dto.titre, dto.auteur)
    }
}