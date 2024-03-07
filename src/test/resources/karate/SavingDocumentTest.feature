Feature: Document API

  Scenario: happy path to create a customer with document

    * def customerId = null

      # create customer
    * def payload = read('/data/customer-request.json')
    * set payload.firstName = 'Jonh'
    * set payload.lastName = 'Coireall'
    * set payload.birthday = '1984-04-25'
    * def response = call read('CustomerServiceClient.feature@CreateCustomer') {"payload": '#(payload)'}
    * match response.status == 201
    * def customerId = response.body.id
    * match customerId != null

    # create document
    * def params = {'customerId': '#(customerId)'}
    * def payload = read('/data/document-request.json')
    * set payload.type = 'PASSPORT'
    * set payload.documentNumber = 'FK937848U'
    * def response = call read('DocumentServiceClient.feature@CreateDocument') {"params": '#(params)', "payload": '#(payload)'}
    * match response.status == 201
    * def documentId = response.body.id
    * match documentId != null

    # update document
    * def params = {'customerId': '#(customerId)', 'documentId': '#(documentId)'}
    * def payload = read('/data/document-request.json')
    * set payload.type = 'PPS'
    * set payload.documentNumber = 'R184728FB'

    * def response = call read('DocumentServiceClient.feature@UpdateDocument') {"params": '#(params)', "payload": '#(payload)'}
    * match response.status == 200

    # get document
    * def params = {'customerId': '#(customerId)', 'documentId': '#(documentId)'}
    * print params
    * def response = call read('DocumentServiceClient.feature@GetDocumentById') {"params": '#(params)'}
    * print response
    * match response.status == 200
    * match response.body.type == 'PPS'
    * match response.body.documentNumber == 'R184728FB'

    # delete document
    * def params = {'customerId': '#(customerId)', 'documentId': '#(documentId)'}
    * def response = call read('DocumentServiceClient.feature@DeleteDocument') {"params": '#(params)'}
    * match response.status == 204
    * def response = call read('DocumentServiceClient.feature@GetDocumentById') {"params": '#(params)'}
    * match response.status == 404

    # delete customer
    * def params = {'customerId': '#(customerId)'}
    * def response = call read('CustomerServiceClient.feature@DeleteCustomer') {"params": '#(params)'}
    * match response.status == 204
    * print params
    * def response = call read('CustomerServiceClient.feature@GetCustomerById') {"params": '#(params)'}
    * print response.body
    * print response.status
    * match response.status == 404
