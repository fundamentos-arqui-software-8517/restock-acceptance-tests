Feature: Profile Management
  As a platform user
  I want to manage my profile information
  So that I can ensure my data is correct and up to date

  Scenario: View profile information
    Given the user is logged in to the platform
    When they navigate to the profile section
    Then they can see their current profile information displayed

  Scenario: Edit basic profile data
    Given the user is on the profile page
    When they update their basic information and confirm the changes
    Then the system saves the updated data and displays a confirmation message

  Scenario: Configure system preferences
    Given the user is on the profile settings page
    When they modify their system preferences
    Then the preferences are saved and applied to their account