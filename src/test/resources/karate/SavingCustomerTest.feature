Feature: Customer API

  Scenario: happy path to create a customer

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

      # update customer
    * def params = {'customerId': '#(customerId)'}
    * def payload = read('/data/customer-request.json')
    * set payload.firstName = 'Jonh'
    * set payload.lastName = 'Murchadh'
    * set payload.birthday = '1983-08-11'

    * def response = call read('CustomerServiceClient.feature@UpdateCustomer') {"params": '#(params)', "payload": '#(payload)'}
    * print response
    * match response.status == 200

    # get customer
    * def params = {'customerId': '#(customerId)'}
    * def response = call read('CustomerServiceClient.feature@GetCustomerById') {"params": '#(params)'}

    * match response.status == 200
    * match response.body.firstName == 'Jonh'
    * match response.body.lastName == 'Murchadh'
    * match response.body.birthday == '1983-08-11'

    # delete customer
    * def params = {'customerId': '#(customerId)'}
    * def response = call read('CustomerServiceClient.feature@DeleteCustomer') {"params": '#(params)'}
    * match response.status == 204
    * def response = call read('CustomerServiceClient.feature@GetCustomerById') {"params": '#(params)'}
    * match response.status == 404
