package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class DeviceMonitoringSteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    @Given("a device is connected")
    public void aDeviceIsConnected() {
    }

    @When("telemetry is received")
    public void telemetryIsReceived() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/devices/DEV-001/status");
    }

    @Then("the device status becomes healthy")
    public void theDeviceStatusBecomesHealthy() {
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("healthy"));
    }

    @Given("a device exceeds threshold values")
    public void aDeviceExceedsThresholdValues() {
    }

    @When("telemetry is processed")
    public void telemetryIsProcessed() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/devices/DEV-001/status");
    }

    @Then("the device status becomes warning")
    public void theDeviceStatusBecomesWarning() {
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("warning"));
    }
}
