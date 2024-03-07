@Ignore
Feature: Email REST API Client

  @CreateEmail
  Scenario: Create email
    * call read('customer-service-api/EmailApi.feature@CreateEmail')

  @UpdateEmail
  Scenario: Update email
    * call read('customer-service-api/EmailApi.feature@UpdateEmail')

  @GetEmailById
  Scenario: Get email
    * call read('customer-service-api/EmailApi.feature@GetEmailById')

  @GetEmails
  Scenario: Get emails
    * call read('customer-service-api/EmailApi.feature@GetEmails')

  @DeleteEmail
  Scenario: Delete email
    * call read('customer-service-api/EmailApi.feature@DeleteEmail')