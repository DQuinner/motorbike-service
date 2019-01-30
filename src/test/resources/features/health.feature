Feature: Actuator Endpoints
  Scenario: client makes call to GET /health
    When the client calls GET /health
    Then the client receives response status code of 200