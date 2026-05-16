Feature: User Login
  As a registered user
  I want to log in to the system
  So that I can securely access my account

  Scenario: Successful login
    Given the user has a registered account
    When they enter their valid credentials email and password
    Then they access their account successfully

  Scenario: Login with invalid credentials
    Given the user has not logged in
    When they enter an incorrect email or password
    Then a message is shown indicating the credentials are not valid and access is denied