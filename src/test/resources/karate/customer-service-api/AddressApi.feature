@Ignore
Feature: AddressApi

  Background:
    * url customerServiceUrl

  @CreateAddress
  Scenario: Create Address
    * def customerId = FunctionsUtils.get(params, "customerId")

    Given path `/v1/customers/${customerId}/addresses`
    And request payload
    When method POST
    Then status 201
    * def body = karate.response.body
    * def status = karate.response.status

  @DeleteAddress
  Scenario: Delete Address
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def addressId = FunctionsUtils.get(params, "addressId")

    Given path `/v1/customers/${customerId}/addresses/${addressId}`

    When method DELETE
    * def body = karate.response.body
    * def status = karate.response.status

  @GetAddressById
  Scenario: Get Address
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def addressId = FunctionsUtils.get(params, "addressId")

    Given path `/v1/customers/${customerId}/addresses/${addressId}`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status

  @UpdateAddress
  Scenario: Update Address
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def addressId = FunctionsUtils.get(params, "addressId")

    Given path `/v1/customers/${customerId}/addresses/${addressId}`
    And request payload

    When method PUT
    * def body = karate.response.body
    * def status = karate.response.status

  @GetAddresses
  Scenario: Get Addresses
    Given path `/v1/customers/${customerId}/addresses`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status