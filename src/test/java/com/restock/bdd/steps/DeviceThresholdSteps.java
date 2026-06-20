package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class DeviceThresholdSteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    @Given("a device exists")
    public void aDeviceExists() {
    }

    @When("the user configures minimum and maximum thresholds")
    public void theUserConfiguresThresholds() {
        Map<String, Object> body = new HashMap<>();
        body.put("deviceId", "DEV-001");
        body.put("minThreshold", 10.0);
        body.put("maxThreshold", 90.0);

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/devices/DEV-001/thresholds");
    }

    @Then("the thresholds are stored successfully")
    public void theThresholdsAreStoredSuccessfully() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
    }

    @Given("thresholds are configured")
    public void thresholdsAreConfigured() {
    }

    @When("the user requests threshold information")
    public void theUserRequestsThresholdInformation() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/devices/DEV-001/thresholds");
    }

    @Then("the configured thresholds are returned")
    public void theConfiguredThresholdsAreReturned() {
        Assertions.assertEquals(200, response.getStatusCode());
    }
}
