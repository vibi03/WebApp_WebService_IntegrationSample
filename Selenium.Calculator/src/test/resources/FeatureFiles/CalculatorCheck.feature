Feature: Mathematical calculation using Calculator in webpage

  @Run
  Scenario Outline: Multiplication process 
    Given User able to launch the calculator page
    Then Provide values to perform Addition
    			|value1|value2|
    			|<value1>|<value2>|
    And Check resultant value is displayed
    Then Save output in a file
    
    Examples:
    		 |value1|value2|
    		 |3|3|