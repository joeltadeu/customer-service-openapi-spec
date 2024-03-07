@Ignore
Feature: EmailApi

  Background:
    * url customerServiceUrl

  @CreateEmail
  Scenario: Create Email
    * def customerId = FunctionsUtils.get(params, "customerId")

    Given path `/v1/customers/${customerId}/emails`
    And request payload
    When method POST
    Then status 201
    * def body = karate.response.body
    * def status = karate.response.status

  @DeleteEmail
  Scenario: Delete Email
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def emailId = FunctionsUtils.get(params, "emailId")

    Given path `/v1/customers/${customerId}/emails/${emailId}`

    When method DELETE
    * def body = karate.response.body
    * def status = karate.response.status

  @GetEmailById
  Scenario: Get Email
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def emailId = FunctionsUtils.get(params, "emailId")

    * print emailId
    Given path `/v1/customers/${customerId}/emails/${emailId}`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status

  @UpdateEmail
  Scenario: Update Email
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def emailId = FunctionsUtils.get(params, "emailId")

    Given path `/v1/customers/${customerId}/emails/${emailId}`
    And request payload

    When method PUT
    * def body = karate.response.body
    * def status = karate.response.status

  @GetEmails
  Scenario: Get Emails
    Given path `/v1/customers/${customerId}/emails`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status