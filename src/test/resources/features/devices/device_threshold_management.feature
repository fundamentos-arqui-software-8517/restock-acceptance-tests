Feature: Manage device thresholds

  Scenario: Configure thresholds
    Given a device exists
    When the user configures minimum and maximum thresholds
    Then the thresholds are stored successfully

  Scenario: Retrieve thresholds
    Given thresholds are configured
    When the user requests threshold information
    Then the configured thresholds are returned
