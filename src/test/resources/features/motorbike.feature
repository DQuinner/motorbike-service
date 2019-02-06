Feature: Motorbike API

  Scenario: client makes call to GET /motorbikes with no motorbikes in the database
    Given no motorbikes exist in the database
    When the client calls GET /motorbikes
    Then the client receives response status code of 204
    And the response body is empty

  Scenario Outline: client makes call to POST /motorbikes with new motorbike
    Given no motorbike exists in the database of "<make>" "<model>" "<engine>"
    When the client calls POST /motorbikes with motorbike of "<make>" "<model>" "<type>" "<engine>"
    Then the client receives response status code of 201
    And the response body contains created motorbike of "<make>" "<model>" "<type>" "<engine>"
    Examples:
      | make     | model         | type         | engine |
      | BMW      | S1000RR       | Sport        | 1000   |
      | Ducati   | Panigale      | Sport        | 1200   |
      | Yamaha   | XJ6 Diversion | Sport Tourer | 600    |
      | Kawasaki | Ninja ZX-6R   | Sport        | 600    |
      | Honda    | CBR1000RR     | Sport        | 1000   |
      | KTM      | Adventurer    | Tourer       | 1200   |

  Scenario Outline: client makes call to POST /motorbikes with motorbike that exists in the database
    Given motorbike exists in the database of "<make>" "<model>" "<engine>"
    When the client calls POST /motorbikes with motorbike of "<make>" "<model>" "<type>" "<engine>"
    Then the client receives response status code of 409
    And the response body is empty
    Examples:
      | make     | model         | type         | engine |
      | BMW      | S1000RR       | Sport        | 1000   |
      | Ducati   | Panigale      | Sport        | 1200   |
      | Yamaha   | XJ6 Diversion | Sport Tourer | 600    |
      | Kawasaki | Ninja ZX-6R   | Sport        | 600    |
      | Honda    | CBR1000RR     | Sport        | 1000   |
      | KTM      | Adventurer    | Tourer       | 1200   |

  Scenario Outline: client makes call to GET /motorbikes with motorbikes in the database
    Given motorbike exists in the database of "<make>" "<model>" "<engine>"
    When the client calls GET /motorbikes
    Then the client receives response status code of 200
    And the response body contains array with created motorbike of "<make>" "<model>" "<type>" "<engine>"
    Examples:
      | make     | model         | type         | engine |
      | BMW      | S1000RR       | Sport        | 1000   |
      | Ducati   | Panigale      | Sport        | 1200   |
      | Yamaha   | XJ6 Diversion | Sport Tourer | 600    |
      | Kawasaki | Ninja ZX-6R   | Sport        | 600    |
      | Honda    | CBR1000RR     | Sport        | 1000   |
      | KTM      | Adventurer    | Tourer       | 1200   |


