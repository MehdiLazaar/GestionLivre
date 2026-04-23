package com.mehdiynov.gestionLivre.infrastructure.driving.controller

import com.mehdiynov.gestionLivre.domain.model.Livre
import com.mehdiynov.gestionLivre.domain.usecase.AjoutLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ListeLivreUsecase
import com.mehdiynov.gestionLivre.domain.usecase.ReserverLivreUsecase
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(BookController::class)
@Import(RestExceptionHandler::class)
class BookControllerIT : FunSpec() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var ajoutLivreUsecase: AjoutLivreUsecase

    @MockkBean
    lateinit var listeLivreUsecase: ListeLivreUsecase

    @MockkBean
    lateinit var reserverLivreUsecase: ReserverLivreUsecase

    init {
        extension(SpringExtension)

        beforeTest {
            clearMocks(ajoutLivreUsecase, listeLivreUsecase, reserverLivreUsecase)
        }

        test("GET /books retourne la liste avec disponibilite") {
            every { listeLivreUsecase.execute() } returns listOf(
                Livre("Livre mehdi", "Lazaar", false),
                Livre("Domain-Driven Design", "Eric Evans", true)
            )

            mockMvc.get("/books") {
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content {
                    json(
                        """
                        [
                          {
                            "titre": "Livre mehdi",
                            "auteur": "Lazaar",
                            "disponible": true
                          },
                          {
                            "titre": "Domain-Driven Design",
                            "auteur": "Eric Evans",
                            "disponible": false
                          }
                        ]
                        """.trimIndent()
                    )
                }
            }

            verify(exactly = 1) { listeLivreUsecase.execute() }
        }

        test("POST /books cree un livre") {
            every { ajoutLivreUsecase.execute("Livre mehdi", "Lazaar") } just runs

            mockMvc.post("/books") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = """
                    {
                      "titre": "Livre mehdi",
                      "auteur": "Lazaar"
                    }
                """.trimIndent()
            }.andExpect {
                status { isCreated() }
            }

            verify(exactly = 1) {
                ajoutLivreUsecase.execute("Livre mehdi", "Lazaar")
            }
        }

        test("POST /books retourne 400 si le titre est vide") {
            mockMvc.post("/books") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = """
                    {
                      "titre": "",
                      "auteur": "Lazaar"
                    }
                """.trimIndent()
            }.andExpect {
                status { isBadRequest() }
            }

            verify(exactly = 0) { ajoutLivreUsecase.execute(any(), any()) }
        }

        test("POST /books/reserve reserve un livre") {
            every { reserverLivreUsecase.execute("Livre mehdi", "Lazaar") } just runs

            mockMvc.post("/books/reserve") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = """
                    {
                      "titre": "Livre mehdi",
                      "auteur": "Lazaar"
                    }
                """.trimIndent()
            }.andExpect {
                status { isNoContent() }
            }

            verify(exactly = 1) {
                reserverLivreUsecase.execute("Livre mehdi", "Lazaar")
            }
        }

        test("POST /books/reserve retourne 400 si le livre est deja reserve") {
            every {
                reserverLivreUsecase.execute("Livre mehdi", "Lazaar")
            } throws IllegalArgumentException("Le livre est déjà réservé")

            mockMvc.post("/books/reserve") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = """
                    {
                      "titre": "Livre mehdi",
                      "auteur": "Lazaar"
                    }
                """.trimIndent()
            }.andExpect {
                status { isBadRequest() }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.message") { value("Le livre est déjà réservé") }
            }
        }

        test("POST /books/reserve retourne 400 si le body est invalide") {
            mockMvc.post("/books/reserve") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = """
                    {
                      "titre": "",
                      "auteur": ""
                    }
                """.trimIndent()
            }.andExpect {
                status { isBadRequest() }
            }

            verify(exactly = 0) { reserverLivreUsecase.execute(any(), any()) }
        }
    }
}
