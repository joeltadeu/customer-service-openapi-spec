@Ignore
Feature: Address REST API Client

  @CreateAddress
  Scenario: Create address
    * call read('customer-service-api/AddressApi.feature@CreateAddress')

  @UpdateAddress
  Scenario: Update address
    * call read('customer-service-api/AddressApi.feature@UpdateAddress')

  @GetAddressById
  Scenario: Get address
    * call read('customer-service-api/AddressApi.feature@GetAddressById')

  @GetAddresses
  Scenario: Get addresses
    * call read('customer-service-api/AddressApi.feature@GetAddresss')

  @DeleteAddress
  Scenario: Delete address
    * call read('customer-service-api/AddressApi.feature@DeleteAddress')