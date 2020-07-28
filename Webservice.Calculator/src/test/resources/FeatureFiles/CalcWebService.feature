Feature: Calculator addition successful scenario


  @Run
  Scenario Outline: 1: To verify addition operation in web service working fine
    Given the consumer requests to "Web Service"
    And the Soap request contains values as
      | value1       | value2       |
      | <value1> 	   | <value2>     |
    And the HTTP SOAP post request is made
    Then the HTTP response is "<StatusCode>"
    And the response nodes are validated successfully for "WebServiceNodes" Service
    And the following details are returned as successful XML response:
      | Response Item      |
      | AddResult          |

    Examples: 
      | value1   | value2   |StatusCode|
      | 3 	     | 3        |200 			 |