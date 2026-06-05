Feature: Backend load balancing with NGINX
  As the Restock technical team
  I want NGINX to distribute requests across multiple backend instances
  So that the system can improve availability and reduce dependency on a single backend instance

  Background:
    Given NGINX is configured with an upstream named restock_backend
    And backend-1 is running
    And backend-2 is running
    And both backend instances are connected to MongoDB and Redis

  Scenario: Requests are distributed between backend instances
    When the user sends multiple requests to /api/v1/supplies
    Then NGINX should distribute the requests between backend-1 and backend-2
    And the system should respond with status 200

  Scenario: Service continuity when one backend instance stops
    Given backend-1 becomes unavailable
    When the user sends a request to /api/v1/supplies
    Then NGINX should forward the request to backend-2
    And the system should continue responding with status 200

  Scenario: Service continuity when the other backend instance stops
    Given backend-2 becomes unavailable
    And backend-1 is available
    When the user sends a request to /api/v1/supplies
    Then NGINX should forward the request to backend-1
    And the system should continue responding with status 200