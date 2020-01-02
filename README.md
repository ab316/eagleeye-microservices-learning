# EagleEye

## Introduction
Microservices inventory management application from the book Spring Microservices in Action.

The project uses the following technologies:
* Java 11
* Maven
* Docker
* Spring Boot
* Spring Cloud
  * OAuth with JWT
  * Service discovery with Eureka
  * Reverse proxy with Zuul
  * Tracing with Sleuth and Zipkin
* Postgres Database
* Zipkin for Distributed tracing
* Redis for caching
* Gitea git server for configuration repository

## Application
The application consists of the following microservices
1. Eureka server
1. Zuul server
1. Config server
1. Authentication service
1. Licensing service
1. Organization service

In addition, the following infrastructure components are also present:
1. RabbitMQ
1. PostgreSQL
    1. One for application
    1. One for Gitea server
1. Zipkin
1. Redis
1. Gitea

## Features
### Centralized Configuration
Readme available in config-server

### OAuth 2.0 Authentication / Authorization
Readme available in authentication-service

### Asynchronous Messaging
Organization service published asynchronous updates regarding a new organization or an update of an existing one.
The Licensing service subscribes to these updates to maintain a cache of the organizations for faster lookup and
partial degradation of the application in case the organization service is down (The organizations present in the 
cache can still be served even if the Organization service is down)

### Distributed Tracing
Each call to the Zuul proxy server generates a tracing id that can be found in the HTTP response header
`tmx-correlation-id`. You can search for this tracing id in Zipkin to see the trace of the request

### Caching
The Licensing service caches the organization data received from the Organization service in Redis.
Therefore, the Get license requests for a new organization id will take longer the first time and subsequent
requests will take shorter time as the call to Organization service is not made.

If the organization is updated using the PUT request, the Organization service informs the Licensing service
about the update through RabbitMQ. Consequently, the Licensing service clears the organization from its
cache. The organization is fetched again when required.

The cache improves the performance by reducing the REST calls between the two services. It also makes the
Licensing service resilient to failures in the Organization service.

## Run
### Prerequisites
* JDK 11
* Maven
* Docker
* Docker Compose

### Running with Docker Compose
#### Setup
1. Set encryption key environment variable `export ENCRYPT_KEY=<RANDOM KEY>`
1.  For the the existing passwords, the key is `IAMANENCRYPTIONKEY`
1. Build the project `mvn clean install`
1. Run docker-compose `docker-compose -f docker/docker-compose.yml up -d`
1. Setup Gitea
    1. Go `http://localhost:3000`
    1. setup the administration account
    1. Click the Install Gitea button
    1. Create a new repository call config-repo
    1. Clone the repository on your machine
1. Put configuration in the config-repo
    1. Copy the folder *licensing-service* from *config-server/src/main/resource/config* to the new git repository
    1. `git remote add origin http://localhost:3000/abdullah/config-repo.git`
    1. `git push -u origin master`
1. You will need to do `docker-compose start` the first time since the application will fail due to no config-repo repository present

#### Auth
To access the services, you first need a JSON Web Token (JWT) from the authentication service which is an OAuth2 server.
Use Postman to authenticate at `http://localhost:5555/api/authentication/oauth/token`:
1. Select POST request
1. Set Authorization to Basic Auth with username `eagleeye` and password `thisismysecret`. These are the client id and
client secret respectively.
1. Use form-data body type to supply the following fields
    1. grant_type = password
    1. scope = webclient
    1. credentials:
        1. Normal user: username/password: abdullah
        1. Admin user: username/password: admin
1. Send the request and copy the value of the `access_token` field in the response. This is the JWT.
1. Use the JWT as Bearer Token in calls to other services.

You can get the user info at `http://localhost:5555/api/authentication/user`. This request also required the JWT

#### Access the Services
* Licensing service at `http://localhost:5555/api/licensing`
* Organization service at `http://localhost:5555/api/organization`
* Config service at `http://localhost:8888`
* Postgres database at `localhost:5432`
* Zipkin at `http://localhost:9411`
* Gitea git server at `http://localhost:3000`
* Gitea git server postgres database at `http://localhost:5433`


### Example REST Requests
#### Authentication Service
##### Sign In
```
POST http://localhost:5555/api/authentication/oauth/token
Basic Auth
    username: eagleeye
    password: thisismysecret
Body: Form Data
    grant_type: password
    scope:      webclient
    username:   abdullah
    password:   abdullah
```

#### Organization Service
##### Create Organization
```
POST http://localhost:5555/api/organization/v1/organizations/
{
	"name": "GitHub",
	"contactName": "Abdullah Baig",
	"contactEmail": "firstname.lastname@git.com",
	"contactPhone": "+123545"
}

Bearer Token: The access_token is response from the Sign In request
```

##### Update Organization
```
PUT http://localhost:5555/api/organization/v1/organizations/
{
	"id": "{organizationId}",
	"name": "GitHub",
	"contactName": "Abdullah Baig",
	"contactEmail": "lastname.firstname@git.com",
	"contactPhone": "098775533"
}
```

##### GET Organization
`GET http://localhost:5555/api/organization/v1/organizations/{organizationId}`

##### DELETE Organization
`DELETE http://localhost:5555/api/organization/v1/organizations/{organizationId}`


#### Licensing Service
##### Create License
```
POST http://localhost:5555/api/licensing/v1/licenses/
{
	"organizationId": "{organizationId}",
	"productName": "MS Office",
	"licenseType": "seat"
}
```

##### Get Licenses by Organization
`GET http://localhost:5555/api/licensing/v1/licenses/organization/{organizationId}`

##### Get Licenses by Id
`GET http://localhost:5555/api/licensing/v1/licenses/{licenseId}`


#### Config Server
##### Get Configuration
`GET http://localhost:8888/licensing-service/default`

## Future Work
The following tasks are planned for the future:
* CI/CD build and deploy pipeline
* AWS template for automated deployment of infrastructure
