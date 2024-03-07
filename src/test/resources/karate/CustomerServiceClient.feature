@Ignore
Feature: Customer REST API Client

  @CreateCustomer
  Scenario: Create customer
    * call read('customer-service-api/CustomerApi.feature@CreateCustomer')

  @UpdateCustomer
  Scenario: Update customer
    * call read('customer-service-api/CustomerApi.feature@UpdateCustomer')

  @GetCustomerById
  Scenario: Get customer
    * call read('customer-service-api/CustomerApi.feature@GetCustomerById')

  @GetCustomers
  Scenario: Get customers
    * call read('customer-service-api/CustomerApi.feature@GetCustomers')

  @DeleteCustomer
  Scenario: Delete customer
    * call read('customer-service-api/CustomerApi.feature@DeleteCustomer')