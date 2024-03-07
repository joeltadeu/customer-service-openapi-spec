Feature: String boot Actuator check

  Background:
    * url customerServiceUrl

  Scenario: actuator available
    Given path '/actuator'
    When method get
    Then status 200
