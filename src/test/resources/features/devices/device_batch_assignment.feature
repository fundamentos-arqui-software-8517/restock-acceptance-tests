Feature: Assign batch to device

  Scenario: Assign batch successfully
    Given a device exists
    And a batch exists
    When the user assigns the batch to the device
    Then the batch is linked to the device

  Scenario: Remove assigned batch
    Given a device has an assigned batch
    When the user removes the batch assignment
    Then the device has no associated batch
