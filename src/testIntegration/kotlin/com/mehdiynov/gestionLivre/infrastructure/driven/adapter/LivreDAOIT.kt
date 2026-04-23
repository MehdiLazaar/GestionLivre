package com.mehdiynov.gestionLivre.infrastructure.driving.controller

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.usecase.AjoutLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ListeLivreUsecase
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(BookController::class)
class BookControllerIT(
    @MockkBean private val ajoutLivreUsecase: AjoutLivreUsecase,
    @MockkBean private val listeLivreUsecase: ListeLivreUsecase,
    private val mockMvc: MockMvc
) : FunSpec({

    extension(SpringExtension)

    test("rest route get books") {
        every { listeLivreUsecase.execute() } returns listOf(
            Livre("A", "B")
        )

        mockMvc.get("/books")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content {
                    json(
                        """
                        [
                          {
                            "titre": "A",
                            "auteur": "B"
                          }
                        ]
                        """.trimIndent()
                    )
                }
            }
    }

    test("rest route post book") {
        justRun { ajoutLivreUsecase.execute(any(), any()) }

        mockMvc.post("/books") {
            content = """
                {
                  "titre": "Les Misérables",
                  "auteur": "Victor Hugo"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }

        verify(exactly = 1) {
            ajoutLivreUsecase.execute("Les Misérables", "Victor Hugo")
        }
    }

    test("rest route post book should return 400 when titre is blank") {
        justRun { ajoutLivreUsecase.execute(any(), any()) }

        mockMvc.post("/books") {
            content = """
                {
                  "titre": "",
                  "auteur": "Victor Hugo"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 0) { ajoutLivreUsecase.execute(any(), any()) }
    }
})
