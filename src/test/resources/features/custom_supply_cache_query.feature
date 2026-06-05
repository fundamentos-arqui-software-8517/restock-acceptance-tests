Feature: Custom Supplies query using Redis Cache
  As a business administrator
  I want to query custom supplies using Redis cache
  So that frequent inventory queries can be answered faster

  Background:
    Given the Restock backend is running
    And Redis is available
    And MongoDB contains registered custom supplies
    And the administrator is authenticated

  Scenario: First query of custom supplies by account
    When the administrator requests the custom supplies by account ID
    Then the system retrieves the information from MongoDB
    And the system stores the result in Redis using the account cache key
    And the response status should be 200

  Scenario: Second query of custom supplies by account from Redis
    Given the custom supplies query result is already stored in Redis
    When the administrator requests the custom supplies by account ID again
    Then the system reuses the cached information from Redis
    And the returned custom supplies should match the previously stored result
    And the response status should be 200

  Scenario: Query a custom supply by ID using Redis
    When the administrator requests a custom supply by its ID
    Then the system retrieves the custom supply information
    And the system stores the result in Redis using the custom supply ID
    And the response status should be 200

  Scenario: Query all custom supplies using Redis
    When the administrator requests all custom supplies
    Then the system retrieves the information from MongoDB
    And the system stores the result in Redis using the global custom supplies cache key
    And the response status should be 200