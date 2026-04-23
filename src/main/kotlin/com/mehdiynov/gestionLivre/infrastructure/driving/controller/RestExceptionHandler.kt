package com.mehdiynov.gestionLivre.infrastructure.driving.controller

import com.mehdiynov.gestionLivre.infrastructure.driving.controller.dto.ErrorDTO
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorDTO> {
        return ResponseEntity
            .badRequest()
            .contentType(APPLICATION_JSON)
            .body(ErrorDTO(ex.message ?: "Bad request"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorDTO> {
        val message = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Validation error"

        return ResponseEntity
            .badRequest()
            .contentType(APPLICATION_JSON)
            .body(ErrorDTO(message))
    }
}
