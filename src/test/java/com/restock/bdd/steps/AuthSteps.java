package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    // ---- REGISTRO ----

    @Given("the visitor does not have an account")
    public void theVisitorDoesNotHaveAnAccount() {
        // estado inicial, no se necesita acción
    }

    @When("they complete the registration form with business name, email, password and role")
    public void theyCompleteTheRegistrationForm() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "testuser_" + System.currentTimeMillis());
        body.put("password", "SecurePass123!");
        body.put("roles", new String[]{"ROLE_RETAILER"});

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/authentication/sign-up");
    }

    @Then("their account is created and they log in as administrator")
    public void theirAccountIsCreated() {
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("they enter an email with invalid format and a weak password")
    public void theyEnterInvalidEmailAndWeakPassword() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "not-an-email");
        body.put("password", "123");
        body.put("roles", new String[]{"ROLE_RETAILER"});

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/authentication/sign-up");
    }

    @Then("a message is shown indicating the data is not valid and registration is not completed")
    public void aMessageIsShownDataNotValid() {
        Assertions.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 422);
    }

    @When("they enter an email already associated with an existing account")
    public void theyEnterAnExistingEmail() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "existing@restock.com");
        body.put("password", "SecurePass123!");
        body.put("roles", new String[]{"ROLE_RETAILER"});

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/authentication/sign-up");
    }

    @Then("a message is shown indicating the registration could not be completed")
    public void aMessageIsShownRegistrationFailed() {
        Assertions.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 409);
    }

    // ---- LOGIN ----

    @Given("the user has a registered account")
    public void theUserHasARegisteredAccount() {
        // precondición: existe usuario en la BD
    }

    @When("they enter their valid credentials email and password")
    public void theyEnterValidCredentials() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "existing@restock.com");
        body.put("password", "SecurePass123!");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/authentication/sign-in");
    }

    @Then("they access their account successfully")
    public void theyAccessTheirAccount() {
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Given("the user has not logged in")
    public void theUserHasNotLoggedIn() {
        // estado inicial
    }

    @When("they enter an incorrect email or password")
    public void theyEnterIncorrectCredentials() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "wrong@restock.com");
        body.put("password", "WrongPassword!");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/authentication/sign-in");
    }

    @Then("a message is shown indicating the credentials are not valid and access is denied")
    public void aMessageIsShownInvalidCredentials() {
        Assertions.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 401);
    }
}