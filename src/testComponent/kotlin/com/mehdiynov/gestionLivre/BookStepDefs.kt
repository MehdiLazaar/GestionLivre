package com.mehdiynov.gestionLivre

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class BookStepDefs {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    private lateinit var lastResponse: ValidatableResponse

    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        jdbcTemplate.update("DELETE FROM livre", MapSqlParameterSource())
    }

    @Given("the user creates the book {string} written by {string}")
    fun createBook(titre: String, auteur: String) {
        lastResponse = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "titre": "$titre",
                  "auteur": "$auteur"
                }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
    }

    @When("the user reserves the book {string} written by {string}")
    fun reserveBook(titre: String, auteur: String) {
        lastResponse = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "titre": "$titre",
                  "auteur": "$auteur"
                }
                """.trimIndent()
            )
            .`when`()
            .post("/books/reserve")
            .then()
    }

    @When("the user retrieves all books")
    fun getAllBooks() {
        lastResponse = RestAssured
            .given()
            .`when`()
            .get("/books")
            .then()
    }

    @Then("the response status should be {int}")
    fun responseStatusShouldBe(status: Int) {
        lastResponse.extract().statusCode() shouldBe status
    }

    @And("the response should contain the book {string} written by {string} as unavailable")
    fun responseShouldContainUnavailableBook(titre: String, auteur: String) {
        val body = lastResponse.extract().body().jsonPath()
        val titres = body.getList<String>("titre")
        val auteurs = body.getList<String>("auteur")
        val disponibilites = body.getList<Boolean>("disponible")

        val index = titres.indexOf(titre)
        index shouldBe auteurs.indexOf(auteur)
        disponibilites[index] shouldBe false
    }

    @And("the error message should be {string}")
    fun errorMessageShouldBe(message: String) {
        val body = lastResponse.extract().body().jsonPath()
        body.getString("message") shouldBe message
    }
}