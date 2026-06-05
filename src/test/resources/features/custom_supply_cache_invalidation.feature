Feature: Custom Supplies cache invalidation
  As a business administrator
  I want cached custom supply data to be invalidated after write operations
  So that users do not see outdated inventory information

  Background:
    Given the Restock backend is running
    And Redis is available
    And the administrator is authenticated
    And custom supply information is already stored in Redis

  Scenario: Cache invalidation after creating a custom supply
    When the administrator creates a new custom supply
    Then the system stores the new custom supply in MongoDB
    And the system invalidates the cached list of all custom supplies
    And the system invalidates the cached custom supplies by account
    And the next query should return the newly created custom supply

  Scenario: Cache invalidation after updating a custom supply
    When the administrator updates an existing custom supply
    Then the system updates the custom supply in MongoDB
    And the system invalidates the cached custom supply by ID
    And the system invalidates the cached custom supplies lists
    And the next query should return the updated custom supply information

  Scenario: Cache invalidation after deleting a custom supply
    When the administrator deletes an existing custom supply
    Then the system removes the custom supply from MongoDB
    And the system invalidates the cached custom supply by ID
    And the system invalidates the cached custom supplies lists
    And the next query should not return the deleted custom supply