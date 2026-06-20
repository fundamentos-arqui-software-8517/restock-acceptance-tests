package com.restock.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class DeviceSteps {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";

    @Given("devices exist in the platform")
    public void devicesExistInThePlatform() {
    }

    @When("the user requests the device list")
    public void theUserRequestsTheDeviceList() {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/devices");
    }

    @Then("the system returns all registered devices")
    public void theSystemReturnsAllRegisteredDevices() {
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Given("a device exists with id {string}")
    public void aDeviceExistsWithId(String deviceId) {
    }

    @When("the user requests device {string}")
    public void theUserRequestsDevice(String deviceId) {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(BASE_URL + "/api/v1/devices/" + deviceId);
    }

    @Then("the system returns device information")
    public void theSystemReturnsDeviceInformation() {
        Assertions.assertEquals(200, response.getStatusCode());
    }
}
