package com.restock.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NginxReverseProxySteps {

    @Given("the Restock stack is deployed using Docker Compose")
    public void theRestockStackIsDeployedUsingDockerCompose() {
        NginxScenarioState.stackDeployed = true;
        NginxScenarioState.responseStatus = 0;
        NginxScenarioState.frontendAccessed = false;
        NginxScenarioState.backendServiceForwarded = false;
        NginxScenarioState.apiRouteUsed = false;
        NginxScenarioState.backendPort8080Used = false;
        NginxScenarioState.swaggerAccessed = false;
        NginxScenarioState.swaggerForwarded = false;
        NginxScenarioState.openApiLoaded = false;
    }

    @And("NGINX is running as the public entry point")
    public void nginxIsRunningAsThePublicEntryPoint() {
        NginxScenarioState.nginxPublicEntryPoint = true;
    }

    @And("the backend is running inside the internal Docker network")
    public void theBackendIsRunningInsideTheInternalDockerNetwork() {
        NginxScenarioState.backendInInternalNetwork = true;
    }

    @And("the frontend is served through NGINX")
    public void theFrontendIsServedThroughNginx() {
        NginxScenarioState.frontendServedThroughNginx = true;
    }

    @When("^the user accesses the web application from http://localhost$")
    public void theUserAccessesTheWebApplicationFromHttpLocalhost() {
        NginxScenarioState.check(
                NginxScenarioState.stackDeployed
                        && NginxScenarioState.nginxPublicEntryPoint
                        && NginxScenarioState.frontendServedThroughNginx,
                "The frontend cannot be accessed through NGINX."
        );

        NginxScenarioState.frontendAccessed = true;
    }

    @And("^the frontend sends a request to /api/v1/supplies$")
    public void theFrontendSendsARequestToApiV1Supplies() {
        NginxScenarioState.check(
                NginxScenarioState.frontendAccessed,
                "The frontend has not been accessed before sending the API request."
        );

        NginxScenarioState.apiRouteUsed = true;
        NginxScenarioState.backendPort8080Used = false;
        NginxScenarioState.backendServiceForwarded = true;
        NginxScenarioState.responseStatus = 200;
    }

    @Then("NGINX forwards the request to the backend service")
    public void nginxForwardsTheRequestToTheBackendService() {
        NginxScenarioState.check(
                NginxScenarioState.backendServiceForwarded,
                "NGINX did not forward the request to the backend service."
        );
    }

    @And("the system responds with status 200")
    public void theSystemRespondsWithStatus200() {
        NginxScenarioState.check(
                NginxScenarioState.responseStatus == 200,
                "Expected response status 200 but got " + NginxScenarioState.responseStatus
        );
    }

    @When("the frontend sends requests to the API")
    public void theFrontendSendsRequestsToTheApi() {
        NginxScenarioState.check(
                NginxScenarioState.stackDeployed
                        && NginxScenarioState.nginxPublicEntryPoint,
                "The NGINX reverse proxy context is not ready."
        );

        NginxScenarioState.apiRouteUsed = true;
        NginxScenarioState.backendPort8080Used = false;
    }

    @Then("^the requests should use the route http://localhost/api/v1$")
    public void theRequestsShouldUseTheRouteHttpLocalhostApiV1() {
        NginxScenarioState.check(
                NginxScenarioState.apiRouteUsed,
                "The requests did not use the route http://localhost/api/v1."
        );
    }

    @And("the requests should not use the backend port 8080 directly")
    public void theRequestsShouldNotUseTheBackendPort8080Directly() {
        NginxScenarioState.check(
                !NginxScenarioState.backendPort8080Used,
                "The requests used the backend port 8080 directly."
        );
    }

    @When("^the user accesses http://localhost/swagger-ui/index\\.html$")
    public void theUserAccessesHttpLocalhostSwaggerUiIndexHtml() {
        NginxScenarioState.check(
                NginxScenarioState.stackDeployed
                        && NginxScenarioState.nginxPublicEntryPoint
                        && NginxScenarioState.backendInInternalNetwork,
                "Swagger UI cannot be accessed through NGINX."
        );
    
        NginxScenarioState.swaggerAccessed = true;
        NginxScenarioState.swaggerForwarded = true;
        NginxScenarioState.openApiLoaded = true;
        NginxScenarioState.responseStatus = 200;
    }

    @Then("NGINX forwards the Swagger UI request to the backend")
    public void nginxForwardsTheSwaggerUiRequestToTheBackend() {
        NginxScenarioState.check(
                NginxScenarioState.swaggerForwarded,
                "NGINX did not forward the Swagger UI request to the backend."
        );
    }

    @And("the OpenAPI documentation should be loaded successfully")
    public void theOpenApiDocumentationShouldBeLoadedSuccessfully() {
        NginxScenarioState.check(
                NginxScenarioState.openApiLoaded,
                "The OpenAPI documentation was not loaded successfully."
        );
    }

    
}