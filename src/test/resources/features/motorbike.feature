Feature: Motorbike Endpoints
  Scenario: client makes call to GET /motorbikes with no motorbikes in the database
    Given no motorbikes exist in the database
    When the client calls GET '/motorbikes'
    Then the client receives response status code of 204
  Scenario: client makes call to POST /motorbikes with no motorbikes in the database
    Given no motorbikes exist in the database
    When the client calls POST '/motorbikes' with body '{"make":"Test Make","model":"Test Model","type":"Test Type"}'
    Then the client receives response status code of 201
  Scenario: client makes call to POST /motorbikes with motorbike that exists in the database
    Given motorbike with make 'Test Make' and model 'Test Model' exists in the database
    When the client calls POST '/motorbikes' with body '{"make":"Test Make","model":"Test Model","type":"Test Type"}'
    Then the client receives response status code of 409
  Scenario: client makes call to GET /motorbikes with one motorbike in the database
    Given motorbike with make 'Test Make' and model 'Test Model' exists in the database
    When the client calls GET '/motorbikes'
    Then the client receives response status code of 200