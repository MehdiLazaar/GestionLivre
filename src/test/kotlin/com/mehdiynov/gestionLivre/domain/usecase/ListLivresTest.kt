package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe

class ListLivresTest : FunSpec({

    test("retourne une liste vide si aucun livre") {
        val repo = FakeRepositoryLivre()
        val useCase = ListeLivreUsecase(repo)

        val result = useCase.execute()

        result.size shouldBe 0
    }

    test("retourne les livres triés par titre") {
        val repo = FakeRepositoryLivre()
        val useCase = ListeLivreUsecase(repo)

        repo.save(Livre("B", "auteur"))
        repo.save(Livre("A", "auteur"))

        val result = useCase.execute()

        result[0].titre shouldBe "A"
        result[1].titre shouldBe "B"
    }

    test("retourne une liste qui contient tout les livres") {

        checkAll(
            Arb.list(
                Arb.bind(
                    Arb.string(),
                    Arb.string()
                ) { titre, auteur -> Livre(titre, auteur) }
            )
        ) { livres ->

            val repo = FakeRepositoryLivre()
            livres.forEach { repo.save(it) }

            val useCase = ListeLivreUsecase(repo)
            val result = useCase.execute()

            result.shouldContainAll(livres)
        }
    }
})
