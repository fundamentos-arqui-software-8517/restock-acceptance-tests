Feature: User Registration
  As a visitor without an account
  I want to register in the system
  So that I can access as a retail or restaurant administrator

  Scenario: Successful registration
    Given the visitor does not have an account
    When they complete the registration form with business name, email, password and role
    Then their account is created and they log in as administrator

  Scenario: Registration with invalid data
    Given the visitor does not have an account
    When they enter an email with invalid format and a weak password
    Then a message is shown indicating the data is not valid and registration is not completed

  Scenario: Registration with existing email
    Given the visitor does not have an account
    When they enter an email already associated with an existing account
    Then a message is shown indicating the registration could not be completed