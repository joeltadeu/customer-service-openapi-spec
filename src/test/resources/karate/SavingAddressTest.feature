Feature: Address API

  Scenario: happy path to create a customer with address

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

    # create address
    * def params = {'customerId': '#(customerId)'}
    * def payload = read('/data/address-request.json')
    * set payload.street = 'Tamarisk, Piperstown Road'
    * set payload.complement = '110'
    * set payload.city = 'Dublin'
    * set payload.county = 'County Dublin'
    * set payload.country = 'Ireland'
    * set payload.eircode = 'D24EHN2'
    * def response = call read('AddressServiceClient.feature@CreateAddress') {"params": '#(params)', "payload": '#(payload)'}
    * match response.status == 201
    * def addressId = response.body.id
    * match addressId != null

    # update address
    * def params = {'customerId': '#(customerId)', 'addressId': '#(addressId)'}
    * def payload = read('/data/address-request.json')
    * set payload.street = 'Advance business park, M50'
    * set payload.complement = '115'
    * set payload.city = 'Fingal'
    * set payload.county = 'County Dublin'
    * set payload.country = 'Ireland'
    * set payload.eircode = 'K67XF78'

    * def response = call read('AddressServiceClient.feature@UpdateAddress') {"params": '#(params)', "payload": '#(payload)'}
    * print response
    * match response.status == 200

    # get address
    * def params = {'customerId': '#(customerId)', 'addressId': '#(addressId)'}
    * def response = call read('AddressServiceClient.feature@GetAddressById') {"params": '#(params)'}

    * match response.status == 200
    * match response.body.street == 'Advance business park, M50'
    * match response.body.complement == '115'
    * match response.body.city == 'Fingal'
    * match response.body.county == 'County Dublin'
    * match response.body.country == 'Ireland'
    * match response.body.eircode == 'K67XF78'

    # delete address
    * def params = {'customerId': '#(customerId)', 'addressId': '#(addressId)'}
    * def response = call read('AddressServiceClient.feature@DeleteAddress') {"params": '#(params)'}
    * match response.status == 204
    * def response = call read('AddressServiceClient.feature@GetAddressById') {"params": '#(params)'}
    * match response.status == 404

    # delete customer
    * def params = {'customerId': '#(customerId)'}
    * def response = call read('CustomerServiceClient.feature@DeleteCustomer') {"params": '#(params)'}
    * match response.status == 204
    * def response = call read('CustomerServiceClient.feature@GetCustomerById') {"params": '#(params)'}
    * match response.status == 404
