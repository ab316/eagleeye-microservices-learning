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
* Postgres Database

## Application
The application consists of the following microservices
1. Eureka server
1. Zuul server
1. Config server
1. Authentication service
1. Licensing service
1. Organization service

## Run
### Prerequisites
* JDK 11
* Maven
* Docker
* Docker Compose

### Docker Compose
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
* Gitea git server at `http://localhost:3000`
* Gitea git server postgres database at `http://localhost:5433`
