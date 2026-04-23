Feature: Reserve a book

  Scenario: the user reserves an available book
    Given the user creates the book "Livre mehdi" written by "Lazaar"
    When the user reserves the book "Livre mehdi" written by "Lazaar"
    Then the response status should be 204
    When the user retrieves all books
    Then the response status should be 200
    And the response should contain the book "Livre mehdi" written by "Lazaar" as unavailable

  Scenario: the user cannot reserve the same book twice
    Given the user creates the book "Livre mehdi" written by "Lazaar"
    When the user reserves the book "Livre mehdi" written by "Lazaar"
    Then the response status should be 204
    When the user reserves the book "Livre mehdi" written by "Lazaar"
    Then the response status should be 400
    And the error message should be "Le livre est déjà réservé"