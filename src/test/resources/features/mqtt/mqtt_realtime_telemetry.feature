Feature: Receive realtime telemetry through MQTT

  Scenario: Telemetry is received successfully
    Given a device publishes telemetry
    When the MQTT broker receives the message
    Then the telemetry is processed by the server

  Scenario: Telemetry updates device metrics
    Given telemetry is received
    When the message is processed
    Then the latest device metrics are updated
