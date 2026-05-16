package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class InventorySteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    @Given("a supply order has been received")
    public void aSupplyOrderHasBeenReceived() {
        // precondición: sistema operativo
    }

    @When("the administrator registers the entry with quantity and expiration date")
    public void theAdminRegistersEntry() {
        Map<String, Object> body = new HashMap<>();
        body.put("supplyId", "supply-001");
        body.put("quantity", 50);
        body.put("expirationDate", "2025-12-31");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/batches");
    }

    @Then("the units are added to the inventory and the new batch is recorded as available")
    public void theUnitsAreAddedToInventory() {
        Assertions.assertEquals(201, response.getStatusCode());
    }

    @Given("the administrator knows the average consumption of a supply")
    public void theAdminKnowsAverageConsumption() {
        // precondición
    }

    @When("they set a minimum reserve quantity for a specific branch")
    public void theySetsMinimumReserve() {
        Map<String, Object> body = new HashMap<>();
        body.put("supplyId", "supply-001");
        body.put("minimumStock", 10);
        body.put("branchId", "branch-001");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/supplies/minimum-stock");
    }

    @Then("the system marks that value as the critical threshold for generating alerts")
    public void theSystemMarksThreshold() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
    }

    @Given("the administrator tries to enter negative values or leaves required fields empty")
    public void theAdminEntersInvalidValues() {
        // precondición
    }

    @When("they try to confirm the inventory operation")
    public void theyTryToConfirmOperation() {
        Map<String, Object> body = new HashMap<>();
        body.put("supplyId", "");
        body.put("quantity", -5);
        body.put("expirationDate", "");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/batches");
    }

    @Then("the system prevents the registration and indicates which data must be corrected")
    public void theSystemPreventsRegistration() {
        Assertions.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 422);
    }
}