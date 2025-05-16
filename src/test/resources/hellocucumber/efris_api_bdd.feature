Feature: URA EFRIS API Integration

  Scenario Outline: Send invalid device and taxpayer details to getInformation API
    Given I set up the EFRIS API base URI and headers
    And I prepare the request payload with invalid device number "<deviceNo>" and TIN "<tin>"
    When I send a POST request to the getInformation endpoint
    Then the API should return HTTP status code 200
    And the return code should be "<expectedReturnCode>"
    And the return message should contain "<expectedMessage>"

    Examples:
      | deviceNo               | tin         | expectedReturnCode | expectedMessage|
      | TCSdfd48d875295800     | 1000066462  |      400           |Device does not exist|
#      | InvalidDevice123       | 1234567890  |      100           | Invalid device or TIN |
#      | NonExistentDeviceXYZ   | 9999999999  |        100         | Invalid device or TIN |
