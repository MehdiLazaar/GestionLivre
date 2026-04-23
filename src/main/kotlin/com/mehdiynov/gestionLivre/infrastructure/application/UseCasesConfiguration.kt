package com.mehdiynov.gestionLivre.infrastructure.application

import com.mehdiynov.gestionLivre.domain.port.LivreRepository
import com.mehdiynov.gestionLivre.domain.usecase.AjoutLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ListeLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ReserverLivreUsecase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {

    @Bean
    fun ajoutLivreUsecase(livreRepository: LivreRepository): AjoutLivreUsecase {
        return AjoutLivreUsecase(livreRepository)
    }

    @Bean
    fun listeLivreUsecase(livreRepository: LivreRepository): ListeLivreUsecase {
        return ListeLivreUsecase(livreRepository)
    }

    @Bean
    fun reserverLivreUsecase(livreRepository: LivreRepository): ReserverLivreUsecase {
        return ReserverLivreUsecase(livreRepository)
    }
}
