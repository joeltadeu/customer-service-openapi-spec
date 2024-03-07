# Customer Service

## Overview

Microservice created using springboot 3.2, generating the model and the controllers interfaces based on the OpenApi Spec using the [OpenAPI generator plugin](https://github.com/OpenAPITools/openapi-generator). 

In addition to unit tests, automated tests were created for the APIs using the [Karate](https://github.com/karatelabs/karate) platform.

## Entity Relationship Diagram (DER)

![Alt text](_assets/der/customer-service.png?raw=true "Customer DER")

> :information_source: Database scripts can be found in the folder [resources](src/main/resources)

| File                                         | Description                       |
|----------------------------------------------|-----------------------------------|
| [schema.sql](src/main/resources/schema.sql)  | SQL script to create the tables   |
| [data.sql](src/main/resources/data.sql)      | SQL script to populate the tables |

## Endpoints

### Retrieve Customer By Id

| EndPoint           | Method | Description              |
|--------------------|:------:|--------------------------|
| /v1/customers/{id} |  GET   | Retrieve customer by id  |

#### Example

> GET /v1/customers/1

#### Response

`200 - OK`

````json lines
{
  "id": 1,
  "firstName": "Aoife",
  "lastName": "Murphy",
  "birthday": "1980-12-13",
  "addresses": [
    {
      "id": 1,
      "street": "6 Bridge St.",
      "eircode": "N36RP84",
      "city": "Cork",
      "county": "County Cork",
      "country": "Ireland"
    }
  ],
  "emails": [
    {
      "id": 1,
      "type": "WORK",
      "email": "aoife.murphy@aib.ie"
    },
    {
      "id": 2,
      "type": "PERSONAL",
      "email": "aoife.murphy@gmail.com"
    }
  ],
  "documents": [
    {
      "id": 1,
      "type": "PPS",
      "documentNumber": "48378IA"
    },
    {
      "id": 2,
      "type": "PASSPORT",
      "documentNumber": "FA3891IU"
    }
  ]
}
````

### Retrieve Customers List

| EndPoint                                                                                             | Method | Description             |
|------------------------------------------------------------------------------------------------------|:------:|-------------------------|
| /v1/customers?pageNumber={pageNumber}&pageSize={pageSize}&firstName={firstName}&lastName={lastName}  |  GET   | Retrieve customers list |

#### Parameters

| Parameter  |        Description         | Example |
|------------|:--------------------------:|---------|
| pageNumber |        Page number         | 0       |
| pageSize   | Number of records per page | 10      |
| firstName  |         First name         | Aoife   |
| lastName   |         Last name          | Murphy  |

#### Example

> GET /v1/customers?pageSize=10&pageNumber=2&firstName=Aoife&lastName=Murphy

#### Response

`200 - OK`

````json lines
{
  "totalElements": 4,
  "totalPages": 1,
  "size": 10,
  "content": [
    {
      "id": 1,
      "firstName": "Aoife",
      "lastName": "Murphy",
      "birthday": "1980-12-13"
    },
    {
      "id": 2,
      "firstName": "Kelly",
      "lastName": "O’Brien",
      "birthday": "1985-10-09"
    },
    {
      "id": 3,
      "firstName": "Ryan",
      "lastName": "O’Sullivan",
      "birthday": "1988-04-25"
    },
    {
      "id": 4,
      "firstName": "Duffy",
      "lastName": "Connor",
      "birthday": "1975-02-18"
    }
  ],
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "first": true,
  "last": true,
  "numberOfElements": 4,
  "pageable": {
    "offset": 0,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "paged": true,
    "unpaged": false,
    "pageSize": 10,
    "pageNumber": 0
  },
  "empty": false
}
````

### Create Customer

| EndPoint      | Method | Description       |
|---------------|:------:|-------------------|
| /v1/customers |  POST  | Create a customer |

#### Example

> POST /v1/customers

#### Body
````json lines
{
  "firstName": "Irwin",
  "lastName": "Streich",
  "birthday": "1977-09-23"
}
````
#### Response

`201 - Created`

````json lines
{
  "id": 5,
  "firstName": "Irwin",
  "lastName": "Streich",
  "birthday": "1977-09-23"
}
````

### Update Customer

| EndPoint           | Method | Description       |
|--------------------|:------:|-------------------|
| /v1/customers/{id} |  PUT   | Update a customer |

#### Example

> PUT /v1/customers/1

#### Body
````json lines
{
  "firstName": "Irwin",
  "lastName": "Streich",
  "birthday": "1980-05-12"
}
````
#### Response

`200 - OK`

### Delete Customer

| EndPoint           | Method | Description       |
|--------------------|:------:|-------------------|
| /v1/customers/{id} | DELETE | Delete a customer |


#### Example

> DELETE /v1/customers/1

#### Response

`200 - OK`

### Retrieve Address By Id

| EndPoint                                   | Method | Description            |
|--------------------------------------------|:------:|------------------------|
| /v1/customers/{customerId}/addresses/{id}  |  GET   | Retrieve address by id |

#### Example

> GET /v1/customers/1/addresses/1

#### Response

`200 - OK`

````json lines
{
  "id": 1,
  "street": "6 Bridge St.",
  "eircode": "N36RP84",
  "city": "Cork",
  "county": "County Cork",
  "country": "Ireland"
}
````

### Retrieve Addresses List by customer Id

| EndPoint                                                                                                                                                              | Method | Description                            |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------:|----------------------------------------|
| /v1/customers/{customerId}/addresses?pageNumber={pageNumber}&pageSize={pageSize}&street={street}&city={city}&county={county}&country={country}&eircode={eircode} |  GET   | Retrieve addresses list by customer id |

#### Parameters

| Parameter  |        Description         | Example      |
|------------|:--------------------------:|--------------|
| pageNumber |        Page number         | 0            |
| pageSize   | Number of records per page | 10           |
| street     |           Street           | 6 Bridge St. |
| city       |            City            | Cork         |
| county     |           County           | County Cork  |
| country    |          Country           | Ireland      |
| eircode    |          Eircode           | N36RP84      |

#### Example

> GET /v1/customers/1/addresses?pageSize=10&pageNumber=2&street=Bridge&city=Cork&country=Ireland&eircode=N36RP84

#### Response

`200 - OK`

````json lines
{
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "content": [
    {
      "id": 1,
      "street": "6 Bridge St.",
      "eircode": "N36RP84",
      "city": "Cork",
      "county": "County Cork",
      "country": "Ireland"
    }
  ],
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "first": true,
  "last": true,
  "numberOfElements": 1,
  "pageable": {
    "offset": 0,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "paged": true,
    "unpaged": false,
    "pageSize": 10,
    "pageNumber": 0
  },
  "empty": false
}
````

### Create Address

| EndPoint                             | Method | Description                      |
|--------------------------------------|:------:|----------------------------------|
| /v1/customers/{customerId}/addresses |  POST  | Create an address by customer id |

#### Example

> POST /v1/customers/1/addresses

#### Body
````json lines
{
  "street": "Hammes Highway",
  "complement": "103",
  "eircode": "N21RP11",
  "city": "Mosciskifort",
  "county": "Roscommon",
  "country": "Ireland"
}
````
#### Response

`201 - Created`

````json lines
{
  "id": 5,
  "street": "Hammes Highway",
  "complement": "103",
  "eircode": "N21RP11",
  "city": "Mosciskifort",
  "county": "Roscommon",
  "country": "Ireland"
}
````

### Update Address

| EndPoint                                  | Method | Description      |
|-------------------------------------------|:------:|------------------|
| /v1/customers/{customerId}/addresses/{id} |  PUT   | Update a address |

#### Example

> PUT /v1/customers/1/addresses/1

#### Body
````json lines
{
  "street": "Hammes Highway",
  "complement": "105",
  "eircode": "N21RP11",
  "city": "Dublin",
  "county": "Co. Dublin",
  "country": "Ireland"
}
````
#### Response

`200 - OK`

### Delete Address

| EndPoint                                  | Method | Description       |
|-------------------------------------------|:------:|-------------------|
| /v1/customers/{customerId}/addresses/{id} | DELETE | Delete an address |

#### Example

> DELETE /v1/customers/1/addresses/1

#### Response

`200 - OK`

### Retrieve Email By Id

| EndPoint                               | Method | Description          |
|----------------------------------------|:------:|----------------------|
| /v1/customers/{customerId}/emails/{id} |  GET   | Retrieve email by id |

#### Example

> GET /v1/customers/1/emails/1

#### Response

`200 - OK`

````json lines
{
  "id": 1,
  "type": "WORK",
  "email": "aoife.murphy@aib.ie"
}
````

### Retrieve Emails List by customer Id

| EndPoint                                                                                                 | Method | Description                          |
|----------------------------------------------------------------------------------------------------------|:------:|--------------------------------------|
| /v1/customers/{customerId}/emails?pageNumber={pageNumber}&pageSize={pageSize}&email={email}&type={type}  |  GET   | Retrieve emails list by customer id  |

#### Parameters

| Parameter  |        Description         | Example             |
|------------|:--------------------------:|---------------------|
| pageNumber |        Page number         | 0                   |
| pageSize   | Number of records per page | 10                  |
| email      |           Email            | aoife.murphy@aib.ie |
| type       |     Type of the email      | WORK                |

#### Example

> GET /v1/customers/1/emails?pageSize=10&pageNumber=2&email=aoife.murphy&type=WORK

#### Response

`200 - OK`

````json lines
{
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "content": [
    {
      "id": 1,
      "type": "WORK",
      "email": "aoife.murphy@aib.ie"
    },
    {
      "id": 2,
      "type": "PERSONAL",
      "email": "aoife.murphy@gmail.com"
    }
  ],
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "first": true,
  "last": true,
  "numberOfElements": 2,
  "pageable": {
    "offset": 0,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "paged": true,
    "unpaged": false,
    "pageSize": 10,
    "pageNumber": 0
  },
  "empty": false
}
````

### Create Email

| EndPoint                           | Method | Description                    |
|------------------------------------|:------:|--------------------------------|
| /v1/customers/{customerId}/emails  |  POST  | Create an email by customer id |

#### Example

> POST /v1/customers/1/emails

#### Body
````json lines
{
  "type": "WORK",
  "email": "aoife.murphy@guiness.ie"
}
````
#### Response

`201 - Created`

````json lines
{
  "id": 10,
  "type": "WORK",
  "email": "aoife.murphy@guiness.ie"
}
````

### Update Email

| EndPoint                                | Method | Description     |
|-----------------------------------------|:------:|-----------------|
| /v1/customers/{customerId}/emails/{id}  |  PUT   | Update an email |

#### Example

> PUT /v1/customers/1/emails/1

#### Body
````json lines
{
  "type": "PERSONAL",
  "email": "aoife.murphy@hotmail.com"
}
````
#### Response

`200 - OK`

### Delete Email

| EndPoint                               | Method | Description      |
|----------------------------------------|:------:|------------------|
| /v1/customers/{customerId}/emails/{id} | DELETE | Delete an email  |


#### Example

> DELETE /v1/customers/1/emails/1

#### Response

`200 - OK`

### Retrieve Document By Id

| EndPoint                                  | Method | Description             |
|-------------------------------------------|:------:|-------------------------|
| /v1/customers/{customerId}/documents/{id} |  GET   | Retrieve document by id |

#### Example

> GET /v1/customers/1/documents/1

#### Response

`200 - OK`

````json lines
{
  "id": 1,
  "type": "PASSPORT",
  "documentNumber": "FU129837"
}
````

### Retrieve Documents List by customer Id

| EndPoint                                                                                                                     | Method | Description                            |
|------------------------------------------------------------------------------------------------------------------------------|:------:|----------------------------------------|
| /v1/customers/{customerId}/documents?pageNumber={pageNumber}&pageSize={pageSize}&documentNumber={documentNumber}&type={type} |  GET   | Retrieve documents list by customer id |

#### Parameters

| Parameter      |        Description         | Example  |
|----------------|:--------------------------:|----------|
| pageNumber     |        Page number         | 0        |
| pageSize       | Number of records per page | 10       |
| documentNumber |      Document number       | FU198329 |
| type           |    Type of the document    | PASSPORT |

#### Example

> GET /v1/customers/1/documents?pageSize=10&pageNumber=2&documentNumber=FU198829&type=PASSPORT

#### Response

`200 - OK`

````json lines
{
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "content": [
    {
      "id": 1,
      "type": "PASSPORT",
      "documentNumber": "FU193891"
    },
    {
      "id": 2,
      "type": "DRIVER_LICENSE",
      "documentNumber": "5675487548GH"
    }
  ],
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "first": true,
  "last": true,
  "numberOfElements": 2,
  "pageable": {
    "offset": 0,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "paged": true,
    "unpaged": false,
    "pageSize": 10,
    "pageNumber": 0
  },
  "empty": false
}
````

### Create Document

| EndPoint                             | Method | Description                      |
|--------------------------------------|:------:|----------------------------------|
| /v1/customers/{customerId}/documents |  POST  | Create a document by customer id |

#### Example

> POST /v1/customers/1/documents

#### Body
````json lines
{
  "type": "PPS",
  "email": "46751M"
}
````
#### Response

`201 - Created`

````json lines
{
  "id": 10,
  "type": "PPS",
  "email": "46751M"
}
````

### Update Email

| EndPoint                                   | Method | Description       |
|--------------------------------------------|:------:|-------------------|
| /v1/customers/{customerId}/documents/{id}  |  PUT   | Update a document |

#### Example

> PUT /v1/customers/1/documents/1

#### Body
````json lines
{
  "type": "PASSPORT",
  "email": "FU4673761"
}
````
#### Response

`200 - OK`

### Delete Document

| EndPoint                                   | Method | Description       |
|--------------------------------------------|:------:|-------------------|
| /v1/customers/{customerId}/documents/{id}  | DELETE | Delete a document |


#### Example

> DELETE /v1/customers/1/documents/1

#### Response

`200 - OK`

## Tests

Execute the unit tests using the command bellow:
```bash
mvn test 
```

Execute the karate tests using the command bellow:
```bash
mvn test -Dkarate.env=local -Dtest=br.com.customer.karate.KarateTestRunner
```
Results can be found in the [target/karate-reports]() folder
![Alt text](_assets/karate/karate-test-result.png?raw=true "Karate Test Result")


## Documentation and Examples

### Swagger

For the documentation of the APIs, access the link
[http://localhost:9081/swagger-ui/index.html](http://localhost:9081/swagger-ui/index.html)

### Postman collection

> :information_source: Postman collection can be found in the folder [postman](_assets/postman/customer-service.postman_collection.json)

## Build & Run

### Local

```bash
mvn clean install
```
Run the command below to run the application.
```bash
java -jar -Dspring.profiles.active=[PROFILE] customer-service.jar
```

### Docker

to build
```
docker build -f Dockerfile -t customer-service:1.0.0 .
```

to run as a container
```
docker run -d -p 9081:9081  --env PROFILE=local -i -t customer-service:1.0.0
```
### Port

9081


## Version

### 1.0.0

- Spring Boot 3.2
- Java 17
- MySQL 8
- Docker Compose
- Karate tests
- OpenApi Generator

## License
Apache License v2.0


