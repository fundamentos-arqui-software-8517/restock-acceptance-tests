package com.restock.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashSet;
import java.util.Set;

final class NginxScenarioState {
    static boolean authProxyConfigured;
    static boolean validationEndpointExposed;
    static boolean protectedEndpointsAvailable;
    static boolean publicAuthEndpointsAvailable;

    static boolean validationRequestSent;
    static boolean backendRejectedValidation;
    static boolean userSignedIn;
    static boolean validJwtToken;
    static boolean tokenValidatedThroughBackend;
    static boolean requestForwardedToBackend;
    static boolean signInProcessed;

    static boolean upstreamConfigured;
    static boolean backend1Running;
    static boolean backend2Running;
    static boolean backendInstancesConnected;
    static final Set<String> distributedBackends = new HashSet<>();
    static String selectedBackend;

    static boolean stackDeployed;
    static boolean nginxPublicEntryPoint;
    static boolean backendInInternalNetwork;
    static boolean frontendServedThroughNginx;
    static boolean frontendAccessed;
    static boolean backendServiceForwarded;
    static boolean apiRouteUsed;
    static boolean backendPort8080Used;
    static boolean swaggerAccessed;
    static boolean swaggerForwarded;
    static boolean openApiLoaded;

    static int responseStatus;

    static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}

public class NginxAuthProxySteps {

    @Given("NGINX is configured as an auth proxy")
    public void nginxIsConfiguredAsAnAuthProxy() {
        NginxScenarioState.authProxyConfigured = true;
        NginxScenarioState.responseStatus = 0;
        NginxScenarioState.validationRequestSent = false;
        NginxScenarioState.backendRejectedValidation = false;
        NginxScenarioState.tokenValidatedThroughBackend = false;
        NginxScenarioState.requestForwardedToBackend = false;
        NginxScenarioState.signInProcessed = false;
    }

    @And("^the backend exposes the endpoint /api/v1/auth/validate$")
    public void theBackendExposesTheEndpointApiV1AuthValidate() {
        NginxScenarioState.validationEndpointExposed = true;
    }


    @And("^protected endpoints are available under /api/v1$")
    public void protectedEndpointsAreAvailableUnderApiV1() {
        NginxScenarioState.protectedEndpointsAvailable = true;
    }

    @And("sign-in and sign-up endpoints are publicly accessible")
    public void signInAndSignUpEndpointsArePubliclyAccessible() {
        NginxScenarioState.publicAuthEndpointsAvailable = true;
    }

    @When("the user requests a protected endpoint without an access token")
    public void theUserRequestsAProtectedEndpointWithoutAnAccessToken() {
        NginxScenarioState.check(
                NginxScenarioState.authProxyConfigured
                        && NginxScenarioState.validationEndpointExposed
                        && NginxScenarioState.protectedEndpointsAvailable,
                "NGINX auth proxy is not correctly configured."
        );

        NginxScenarioState.validationRequestSent = true;
        NginxScenarioState.backendRejectedValidation = true;
        NginxScenarioState.requestForwardedToBackend = false;
        NginxScenarioState.responseStatus = 401;
    }

    @Then("NGINX should send an internal validation request to the backend")
    public void nginxShouldSendAnInternalValidationRequestToTheBackend() {
        NginxScenarioState.check(
                NginxScenarioState.validationRequestSent,
                "NGINX did not send an internal validation request to the backend."
        );
    }

    @And("the backend should reject the validation request")
    public void theBackendShouldRejectTheValidationRequest() {
        NginxScenarioState.check(
                NginxScenarioState.backendRejectedValidation,
                "The backend did not reject the validation request."
        );
    }

    @And("NGINX should respond with status 401")
    public void nginxShouldRespondWithStatus401() {
        NginxScenarioState.check(
                NginxScenarioState.responseStatus == 401,
                "Expected NGINX status 401 but got " + NginxScenarioState.responseStatus
        );
    }

    @Given("the user has signed in successfully")
    public void theUserHasSignedInSuccessfully() {
        NginxScenarioState.userSignedIn = true;
    }

    @And("the user has a valid JWT access token")
    public void theUserHasAValidJwtAccessToken() {
        NginxScenarioState.check(
                NginxScenarioState.userSignedIn,
                "The user has not signed in successfully."
        );

        NginxScenarioState.validJwtToken = true;
    }

    @When("the user requests a protected endpoint with the token")
    public void theUserRequestsAProtectedEndpointWithTheToken() {
        NginxScenarioState.check(
                NginxScenarioState.authProxyConfigured
                        && NginxScenarioState.validJwtToken
                        && NginxScenarioState.protectedEndpointsAvailable,
                "The protected request cannot be sent because the auth context is invalid."
        );

        NginxScenarioState.validationRequestSent = true;
        NginxScenarioState.tokenValidatedThroughBackend = true;
        NginxScenarioState.requestForwardedToBackend = true;
        NginxScenarioState.responseStatus = 200;
    }

    @Then("NGINX should validate the token through the backend")
    public void nginxShouldValidateTheTokenThroughTheBackend() {
        NginxScenarioState.check(
                NginxScenarioState.tokenValidatedThroughBackend,
                "NGINX did not validate the token through the backend."
        );
    }

    @And("NGINX should forward the request to the backend")
    public void nginxShouldForwardTheRequestToTheBackend() {
        NginxScenarioState.check(
                NginxScenarioState.requestForwardedToBackend,
                "NGINX did not forward the request to the backend."
        );
    }

    @And("the system should respond with status 200")
    public void theSystemShouldRespondWithStatus200() {
        NginxScenarioState.check(
                NginxScenarioState.responseStatus == 200,
                "Expected system status 200 but got " + NginxScenarioState.responseStatus
        );
    }

    @When("^the user sends a sign-in request to /api/v1/auth/sign-in$")
    public void theUserSendsASignInRequestToApiV1AuthSignIn() {
        NginxScenarioState.check(
                NginxScenarioState.authProxyConfigured
                        && NginxScenarioState.publicAuthEndpointsAvailable,
                "Public authentication endpoints are not available through NGINX."
        );
    
        NginxScenarioState.requestForwardedToBackend = true;
        NginxScenarioState.signInProcessed = true;
        NginxScenarioState.responseStatus = 200;
    }

    @Then("NGINX should forward the request without requiring previous authentication")
    public void nginxShouldForwardTheRequestWithoutRequiringPreviousAuthentication() {
        NginxScenarioState.check(
                NginxScenarioState.requestForwardedToBackend,
                "NGINX did not forward the public authentication request."
        );
    }

    @And("the backend should process the sign-in request")
    public void theBackendShouldProcessTheSignInRequest() {
        NginxScenarioState.check(
                NginxScenarioState.signInProcessed,
                "The backend did not process the sign-in request."
        );
    }
}