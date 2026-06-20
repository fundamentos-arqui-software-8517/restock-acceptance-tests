package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class MqttTelemetrySteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    @Given("a device publishes telemetry")
    public void aDevicePublishesTelemetry() {
    }

    @When("the MQTT broker receives the message")
    public void theMqttBrokerReceivesTheMessage() {
        Map<String, Object> body = new HashMap<>();
        body.put("deviceId", "DEV-001");
        body.put("temperature", 25.5);
        body.put("humidity", 60.0);
        body.put("timestamp", System.currentTimeMillis());

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .post(BASE_URL + "/api/v1/telemetry");
    }

    @Then("the telemetry is processed by the server")
    public void theTelemetryIsProcessedByTheServer() {
        Assertions.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
    }

    @Given("telemetry is received")
    public void telemetryIsReceived2() {
    }

    @When("the message is processed")
    public void theMessageIsProcessed() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/devices/DEV-001/metrics/latest");
    }

    @Then("the latest device metrics are updated")
    public void theLatestDeviceMetricsAreUpdated() {
        Assertions.assertEquals(200, response.getStatusCode());
    }
}
