Feature: View registered devices

  Scenario: Retrieve all registered devices
    Given devices exist in the platform
    When the user requests the device list
    Then the system returns all registered devices

  Scenario: Retrieve a specific device
    Given a device exists with id "DEV-001"
    When the user requests device "DEV-001"
    Then the system returns device information
