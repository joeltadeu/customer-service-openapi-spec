@Ignore
Feature: CustomerApi

  Background:
    * url customerServiceUrl

  @CreateCustomer
  Scenario: Create Customer
    Given path `/v1/customers`
    And request payload
    When method POST
    Then status 201
    * def body = karate.response.body
    * def status = karate.response.status

  @DeleteCustomer
  Scenario: Delete Customer
    * def customerId = FunctionsUtils.get(params, "customerId")

    Given path `/v1/customers/${customerId}`

    When method DELETE
    * def body = karate.response.body
    * def status = karate.response.status

  @GetCustomerById
  Scenario: Get Customer
    * def customerId = FunctionsUtils.get(params, "customerId")
    * print '/v1/customers/' + customerId
    Given path `/v1/customers/${customerId}`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status

  @UpdateCustomer
  Scenario: Update Customer
    * def customerId = FunctionsUtils.get(params, "customerId")
    * print payload
    * print customerId
    Given path `/v1/customers/${customerId}`
    And request payload

    When method PUT
    * def body = karate.response.body
    * def status = karate.response.status

  @GetCustomers
  Scenario: Get Customers
    Given path `/v1/customers`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status