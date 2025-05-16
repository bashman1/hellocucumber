Feature: Login Functionality

  @get
  Scenario Outline: User tests GET API
    Given Set Base URL "<base_url>" for API call
    Then Add header "<header_key>" with value "<header_value>"
    When Call "<http_method>" on path "<api_path>"
    Then Check status code equals <expected_status>
    Then response contains "<response_url>" equals "<expected_response_url>"
    Then response contains "headers.<header_key>" equals "<header_value>"

    Examples:
      | base_url   | header_key | header_value | http_method | api_path | expected_status | response_url| expected_response_url|
      | https://httpbin.org | Test | header | GET | /get | 200 | url | https://httpbin.org/get                                 |


  @post
  Scenario Outline: User tests POST API
    Given Set Base URL "<base_url>" for API call
    Then Add header "<header_key>" with value "<header_value>"
    Then Add body with value "<body_content>"
    When Call "<http_method>" on path "<api_path>"
    Then Check status code equals <expected_status>
    Then response contains "<response_url_field>" equals "<expected_response_url>"
    Then response contains "headers.<header_key>" equals "<header_value>"
    Then response contains "<data_field>" equals "<body_content>"
    Then response contains "<json_field>" equals "<json_value>"

    Examples:
      | base_url          | header_key | header_value | body_content               | http_method | api_path | expected_status | response_url_field | expected_response_url      | data_field | json_field       | json_value |
      | https://httpbin.org | Test       | header        | {\"testfield\": \"test\"}      | POST         | /post    | 200             | url                 | https://httpbin.org/post   | data       | json.testfield   | test       |
