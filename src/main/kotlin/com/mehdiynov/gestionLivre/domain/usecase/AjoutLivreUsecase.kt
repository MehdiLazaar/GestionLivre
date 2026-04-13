package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.port.LivreRepository

class AjoutLivreUsecase(private val repository: LivreRepository) {

    fun execute(titre : String, auteur : String){
        val titre = Livre(titre, auteur)
        repository.save(titre)
    }
}