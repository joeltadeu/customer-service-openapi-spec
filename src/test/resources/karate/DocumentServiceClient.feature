@Ignore
Feature: Document REST API Client

  @CreateDocument
  Scenario: Create document
    * call read('customer-service-api/DocumentApi.feature@CreateDocument')

  @UpdateDocument
  Scenario: Update document
    * call read('customer-service-api/DocumentApi.feature@UpdateDocument')

  @GetDocumentById
  Scenario: Get document
    * call read('customer-service-api/DocumentApi.feature@GetDocumentById')

  @GetDocuments
  Scenario: Get documents
    * call read('customer-service-api/DocumentApi.feature@GetDocuments')

  @DeleteDocument
  Scenario: Delete document
    * call read('customer-service-api/DocumentApi.feature@DeleteDocument')