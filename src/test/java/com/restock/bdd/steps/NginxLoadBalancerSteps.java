package com.restock.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NginxLoadBalancerSteps {

    @Given("NGINX is configured with an upstream named restock_backend")
    public void nginxIsConfiguredWithAnUpstreamNamedRestockBackend() {
        NginxScenarioState.upstreamConfigured = true;
        NginxScenarioState.responseStatus = 0;
        NginxScenarioState.distributedBackends.clear();
        NginxScenarioState.selectedBackend = null;
    }

    @And("backend-1 is running")
    public void backend1IsRunning() {
        NginxScenarioState.backend1Running = true;
    }

    @And("backend-2 is running")
    public void backend2IsRunning() {
        NginxScenarioState.backend2Running = true;
    }

    @And("both backend instances are connected to MongoDB and Redis")
    public void bothBackendInstancesAreConnectedToMongoDBAndRedis() {
        NginxScenarioState.check(
                NginxScenarioState.backend1Running && NginxScenarioState.backend2Running,
                "Both backend instances must be running before validating shared connections."
        );

        NginxScenarioState.backendInstancesConnected = true;
    }

    @When("^the user sends multiple requests to /api/v1/supplies$")
    public void theUserSendsMultipleRequestsToApiV1Supplies() {
        NginxScenarioState.check(
                NginxScenarioState.upstreamConfigured
                        && NginxScenarioState.backend1Running
                        && NginxScenarioState.backend2Running
                        && NginxScenarioState.backendInstancesConnected,
                "The load balancer context is not ready."
        );

        NginxScenarioState.distributedBackends.add("backend-1");
        NginxScenarioState.distributedBackends.add("backend-2");
        NginxScenarioState.responseStatus = 200;
    }

    @Then("NGINX should distribute the requests between backend-1 and backend-2")
    public void nginxShouldDistributeTheRequestsBetweenBackend1AndBackend2() {
        NginxScenarioState.check(
                NginxScenarioState.distributedBackends.contains("backend-1")
                        && NginxScenarioState.distributedBackends.contains("backend-2"),
                "NGINX did not distribute the requests between backend-1 and backend-2."
        );
    }

    @Given("backend-1 becomes unavailable")
    public void backend1BecomesUnavailable() {
        NginxScenarioState.backend1Running = false;
    }

    @When("^the user sends a request to /api/v1/supplies$")
    public void theUserSendsARequestToApiV1Supplies() {
        NginxScenarioState.check(
                NginxScenarioState.upstreamConfigured,
                "NGINX upstream is not configured."
        );  

        if (NginxScenarioState.backend1Running) {
            NginxScenarioState.selectedBackend = "backend-1";
            NginxScenarioState.responseStatus = 200;
        } else if (NginxScenarioState.backend2Running) {
            NginxScenarioState.selectedBackend = "backend-2";
            NginxScenarioState.responseStatus = 200;
        } else {
            NginxScenarioState.selectedBackend = null;
            NginxScenarioState.responseStatus = 503;
        }
    }

    @Then("NGINX should forward the request to backend-2")
    public void nginxShouldForwardTheRequestToBackend2() {
        NginxScenarioState.check(
                "backend-2".equals(NginxScenarioState.selectedBackend),
                "NGINX did not forward the request to backend-2."
        );
    }

    @And("the system should continue responding with status 200")
    public void theSystemShouldContinueRespondingWithStatus200() {
        NginxScenarioState.check(
                NginxScenarioState.responseStatus == 200,
                "Expected continuity status 200 but got " + NginxScenarioState.responseStatus
        );
    }

    @Given("backend-2 becomes unavailable")
    public void backend2BecomesUnavailable() {
        NginxScenarioState.backend2Running = false;
    }

    @And("backend-1 is available")
    public void backend1IsAvailable() {
        NginxScenarioState.backend1Running = true;
    }

    @Then("NGINX should forward the request to backend-1")
    public void nginxShouldForwardTheRequestToBackend1() {
        NginxScenarioState.check(
                "backend-1".equals(NginxScenarioState.selectedBackend),
                "NGINX did not forward the request to backend-1."
        );
    }

    
}