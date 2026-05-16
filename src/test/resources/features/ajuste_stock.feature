Feature: Stock Management
  As an administrator
  I want to manage inventory stock
  So that I can keep accurate records of supplies

  Scenario: Register incoming supplies
    Given a supply order has been received
    When the administrator registers the entry with quantity and expiration date
    Then the units are added to the inventory and the new batch is recorded as available

  Scenario: Configure minimum stock level
    Given the administrator knows the average consumption of a supply
    When they set a minimum reserve quantity for a specific branch
    Then the system marks that value as the critical threshold for generating alerts

  Scenario: Attempt to register with invalid data
    Given the administrator tries to enter negative values or leaves required fields empty
    When they try to confirm the inventory operation
    Then the system prevents the registration and indicates which data must be corrected