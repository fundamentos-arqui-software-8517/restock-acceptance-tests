Feature: Monitor device status

  Scenario: Device reports healthy status
    Given a device is connected
    When telemetry is received
    Then the device status becomes healthy

  Scenario: Device reports warning status
    Given a device exceeds threshold values
    When telemetry is processed
    Then the device status becomes warning
