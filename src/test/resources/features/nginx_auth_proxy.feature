Feature: Access validation through NGINX Auth Proxy
  As the Restock technical team
  I want NGINX to validate protected API requests before forwarding them to the backend
  So that access control can be reinforced at the infrastructure layer

  Background:
    Given NGINX is configured as an auth proxy
    And the backend exposes the endpoint /api/v1/auth/validate
    And protected endpoints are available under /api/v1
    And sign-in and sign-up endpoints are publicly accessible

  Scenario: Block a protected request without token
    When the user requests a protected endpoint without an access token
    Then NGINX should send an internal validation request to the backend
    And the backend should reject the validation request
    And NGINX should respond with status 401

  Scenario: Allow a protected request with a valid token
    Given the user has signed in successfully
    And the user has a valid JWT access token
    When the user requests a protected endpoint with the token
    Then NGINX should validate the token through the backend
    And NGINX should forward the request to the backend
    And the system should respond with status 200

  Scenario: Public authentication endpoints remain accessible
    When the user sends a sign-in request to /api/v1/auth/sign-in
    Then NGINX should forward the request without requiring previous authentication
    And the backend should process the sign-in request