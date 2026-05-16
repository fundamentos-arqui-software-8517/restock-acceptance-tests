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

    // ---- GESTIÓN DE PERFIL ----

    @Given("the user is logged in to the platform")
    public void theUserIsLoggedInToThePlatform() {
        // precondición: usuario autenticado
    }

    @When("they navigate to the profile section")
    public void theyNavigateToTheProfileSection() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/profiles/me");
    }

    @Then("they can see their current profile information displayed")
    public void theyCanSeeTheirCurrentProfileInformation() {
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Given("the user is on the profile page")
    public void theUserIsOnTheProfilePage() {
        // precondición
    }

    @When("they update their basic information and confirm the changes")
    public void theyUpdateTheirBasicInformation() {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Juan");
        body.put("lastName", "Pérez");
        body.put("phone", "+51999999999");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .put(BASE_URL + "/api/v1/profiles/me");
    }

    @Then("the system saves the updated data and displays a confirmation message")
    public void theSystemSavesTheUpdatedData() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }

    @Given("the user is on the profile settings page")
    public void theUserIsOnTheProfileSettingsPage() {
        // precondición
    }

    @When("they modify their system preferences")
    public void theyModifyTheirSystemPreferences() {
        Map<String, Object> body = new HashMap<>();
        body.put("language", "es");
        body.put("notifications", true);

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .patch(BASE_URL + "/api/v1/profiles/me/preferences");
    }

    @Then("the preferences are saved and applied to their account")
    public void thePreferencesAreSavedAndApplied() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }
}