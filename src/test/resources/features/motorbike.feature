Feature: Motorbike API

  Scenario: client makes call to GET /motorbikes with no motorbikes in the database
    Given no motorbikes exist in the database
    When the client calls GET /motorbikes
    Then the client receives response status code of 204
    And the response body is empty

  Scenario Outline: client makes call to POST /motorbikes with new motorbike
    Given no motorbike exists in the database of <make> <model>
    When the client calls POST /motorbikes with motorbike of <make> <model> <type>
    Then the client receives response status code of 201
    And the response body contains created motorbike of <make> <model> <type>
    Examples:
      | make     | model         | type         |
      | BMW      | S1000RR       | Sport        |
      | Ducati   | Panigale      | Sport        |
      | Yamaha   | XJ6-Diversion | Sport-Tourer |
      | Kawasaki | Ninja-ZX-6R   | Sport        |
      | Honda    | CBR1000RR     | Sport        |
      | KTM      | Adventurer    | Tourer       |


  Scenario Outline: client makes call to POST /motorbikes with motorbike that exists in the database
    Given motorbike exists in the database of <make> <model>
    When the client calls POST /motorbikes with motorbike of <make> <model> <type>
    Then the client receives response status code of 409
    And the response body is empty
    Examples:
      | make     | model         | type         |
      | BMW      | S1000RR       | Sport        |
      | Ducati   | Panigale      | Sport        |
      | Yamaha   | XJ6-Diversion | Sport-Tourer |
      | Kawasaki | Ninja-ZX-6R   | Sport        |
      | Honda    | CBR1000RR     | Sport        |
      | KTM      | Adventurer    | Tourer       |

  Scenario Outline: client makes call to GET /motorbikes with motorbikes in the database
    Given motorbike exists in the database of <make> <model>
    When the client calls GET /motorbikes
    Then the client receives response status code of 200
    And the response body contains array with created motorbike of <make> <model> <type>
    Examples:
      | make     | model         | type         |
      | BMW      | S1000RR       | Sport        |
      | Ducati   | Panigale      | Sport        |
      | Yamaha   | XJ6-Diversion | Sport-Tourer |
      | Kawasaki | Ninja-ZX-6R   | Sport        |
      | Honda    | CBR1000RR     | Sport        |
      | KTM      | Adventurer    | Tourer       |


