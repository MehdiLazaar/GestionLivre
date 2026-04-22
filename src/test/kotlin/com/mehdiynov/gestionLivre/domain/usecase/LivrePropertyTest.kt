package com.mehdiynov.gestionLivre.domain.usecase

import com.mehdiynov.gestionLivre.domain.model.Livre
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class LivrePropertyTest : FunSpec({

    test("la liste contient tous les livres ajoutés") {
        checkAll<List<String>> { titre ->

            val repo = FakeRepositoryLivre()
            val usecase = ListeLivreUsecase(repo)

            titre.filter { it.isNotBlank() }
                .forEach { repo.save(Livre(it, "Auteur")) }

            val result = usecase.execute()

            result.size shouldBe titre.filter { it.isNotBlank() }.size
        }
    }
})