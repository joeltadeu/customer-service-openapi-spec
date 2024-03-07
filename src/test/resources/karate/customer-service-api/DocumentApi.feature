@Ignore
Feature: DocumentApi

  Background:
    * url customerServiceUrl

  @CreateDocument
  Scenario: Create Document
    * def customerId = FunctionsUtils.get(params, "customerId")

    Given path `/v1/customers/${customerId}/documents`
    And request payload
    When method POST
    Then status 201
    * def body = karate.response.body
    * def status = karate.response.status

  @DeleteDocument
  Scenario: Delete Document
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def documentId = FunctionsUtils.get(params, "documentId")

    Given path `/v1/customers/${customerId}/documents/${documentId}`

    When method DELETE
    * def body = karate.response.body
    * def status = karate.response.status

  @GetDocumentById
  Scenario: Get Document
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def documentId = FunctionsUtils.get(params, "documentId")

    Given path `/v1/customers/${customerId}/documents/${documentId}`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status

  @UpdateDocument
  Scenario: Update Document
    * def customerId = FunctionsUtils.get(params, "customerId")
    * def documentId = FunctionsUtils.get(params, "documentId")

    Given path `/v1/customers/${customerId}/documents/${documentId}`
    And request payload

    When method PUT
    * def body = karate.response.body
    * def status = karate.response.status

  @GetDocuments
  Scenario: Get Documents
    Given path `/v1/customers/${customerId}/documents`

    When method GET
    * def body = karate.response.body
    * def status = karate.response.status