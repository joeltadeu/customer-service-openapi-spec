Feature: Email API

  Scenario: happy path to create a customer with email

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

    # create email
    * def params = {'customerId': '#(customerId)'}
    * def payload = read('/data/email-request.json')
    * set payload.type = 'WORK'
    * set payload.email = 'jonh.coireall@tesco.ie'
    * def response = call read('EmailServiceClient.feature@CreateEmail') {"params": '#(params)', "payload": '#(payload)'}
    * match response.status == 201
    * def emailId = response.body.id
    * match emailId != null

    # update email
    * def params = {'customerId': '#(customerId)', 'emailId': '#(emailId)'}
    * def payload = read('/data/email-request.json')
    * set payload.type = 'PERSONAL'
    * set payload.email = 'jonh.coireall@gmail.com'

    * def response = call read('EmailServiceClient.feature@UpdateEmail') {"params": '#(params)', "payload": '#(payload)'}
    * match response.status == 200

    # get email
    * def params = {'customerId': '#(customerId)', 'emailId': '#(emailId)'}
    * print params
    * def response = call read('EmailServiceClient.feature@GetEmailById') {"params": '#(params)'}
    * print response
    * match response.status == 200
    * match response.body.type == 'PERSONAL'
    * match response.body.email == 'jonh.coireall@gmail.com'

    # delete email
    * def params = {'customerId': '#(customerId)', 'emailId': '#(emailId)'}
    * def response = call read('EmailServiceClient.feature@DeleteEmail') {"params": '#(params)'}
    * match response.status == 204
    * def response = call read('EmailServiceClient.feature@GetEmailById') {"params": '#(params)'}
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
