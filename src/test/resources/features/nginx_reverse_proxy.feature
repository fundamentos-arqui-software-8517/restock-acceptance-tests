Feature: Backend access through NGINX Reverse Proxy
  As a Restock user
  I want frontend requests to be routed through NGINX
  So that backend access can be centralized and the backend service is not accessed directly

  Background:
    Given the Restock stack is deployed using Docker Compose
    And NGINX is running as the public entry point
    And the backend is running inside the internal Docker network
    And the frontend is served through NGINX

  Scenario: Frontend consumes backend through NGINX
    When the user accesses the web application from http://localhost
    And the frontend sends a request to /api/v1/supplies
    Then NGINX forwards the request to the backend service
    And the system responds with status 200

  Scenario: API requests do not use the backend public port
    When the frontend sends requests to the API
    Then the requests should use the route http://localhost/api/v1
    And the requests should not use the backend port 8080 directly

  Scenario: Swagger UI is served through NGINX
    When the user accesses http://localhost/swagger-ui/index.html
    Then NGINX forwards the Swagger UI request to the backend
    And the OpenAPI documentation should be loaded successfully