Feature: Actuator Health API
  Scenario: client makes call to GET /health
    When the client calls GET /actuator/health
    Then the client receives response status code of 200
    And the health status is UP