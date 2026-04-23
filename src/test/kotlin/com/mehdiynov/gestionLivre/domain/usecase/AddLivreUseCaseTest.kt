package com.mehdiynov.gestionLivre.domain.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec

class AddLivreUseCaseTest : FunSpec({



    test("ajoute un livre quand les données sont valides") {
        val repo = FakeRepositoryLivre()
        val useCase = AjoutLivreUsecase(repo)

        useCase.execute("LivreMehdi", "Robert Martin")

        assert(repo.findAll().size == 1)
    }

    test("lance une exception si le titre est vide") {
        val repo = FakeRepositoryLivre()
        val useCase = AjoutLivreUsecase(repo)

        shouldThrow<IllegalArgumentException> {
            useCase.execute("", "author")
        }
    }

    test("lance une exception si l'auteur est vide") {
        val repo = FakeRepositoryLivre()
        val useCase = AjoutLivreUsecase(repo)

        shouldThrow<IllegalArgumentException> {
            useCase.execute("titre", "")
        }
    }
})
