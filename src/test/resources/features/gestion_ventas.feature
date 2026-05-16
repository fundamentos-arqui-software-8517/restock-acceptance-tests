Feature: Sales Management
  As a business administrator
  I want to register and consult sales of products or combos
  So that I can keep the inventory updated and track commercial performance

  Scenario: Register a new sale
    Given the administrator is logged in to the platform
    When they register a sale with the product details and quantity
    Then the sale is recorded and the inventory is updated accordingly

  Scenario: Consult registered sales
    Given the administrator has registered sales in the system
    When they access the sales consultation section
    Then they can see the list of all registered sales

  Scenario: View sale detail
    Given the administrator is on the sales list
    When they select a specific sale
    Then they can see the full detail of that sale including products and total amount