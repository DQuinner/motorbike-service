Feature: Motorbike Endpoints
  Scenario: client makes call to GET /motorbikes with no motorbikes in the database
    Given no motorbikes exist in the database
    When the client calls GET '/motorbikes'
    Then the client receives response status code of 204
    And the response body is empty
  Scenario: client makes call to POST /motorbikes with no motorbikes in the database
    Given no motorbikes exist in the database
    When the client calls POST '/motorbikes' with body '{"make":"Yamaha","model":"XJ6 Diversion","type":"Sports Tourer"}'
    Then the client receives response status code of 201
    And the response body contains created motorbike of 'Yamaha' 'XJ6 Diversion' 'Sports Tourer'
  Scenario: client makes call to POST /motorbikes with motorbike that exists in the database
    Given motorbike exists in the database of 'Yamaha' 'XJ6 Diversion'
    When the client calls POST '/motorbikes' with body '{"make":"Yamaha","model":"XJ6 Diversion","type":"Sports Tourer"}'
    Then the client receives response status code of 409
    And the response body is empty
  Scenario: client makes call to GET /motorbikes with one motorbike in the database
    Given motorbike exists in the database of 'Yamaha' 'XJ6 Diversion'
    When the client calls GET '/motorbikes'
    Then the client receives response status code of 200
    And the response body contains array with created motorbike of 'Yamaha' 'XJ6 Diversion' 'Sports Tourer'