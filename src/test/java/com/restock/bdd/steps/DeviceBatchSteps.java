package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class DeviceBatchSteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    @Given("a batch exists")
    public void aBatchExists() {
    }

    @When("the user assigns the batch to the device")
    public void theUserAssignsTheBatchToTheDevice() {
        Map<String, Object> body = new HashMap<>();
        body.put("batchId", "BATCH-001");

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/devices/DEV-001/batch");
    }

    @Then("the batch is linked to the device")
    public void theBatchIsLinkedToTheDevice() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
    }

    @Given("a device has an assigned batch")
    public void aDeviceHasAnAssignedBatch() {
    }

    @When("the user removes the batch assignment")
    public void theUserRemovesTheBatchAssignment() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .delete(BASE_URL + "/api/v1/devices/DEV-001/batch");
    }

    @Then("the device has no associated batch")
    public void theDeviceHasNoAssociatedBatch() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204);
    }
}
