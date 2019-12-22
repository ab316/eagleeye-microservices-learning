# EagleEye

## Introduction
Microservices inventory management application from the book Spring Microservices in Action.

The project uses the following technologies:
* Java 11
* Maven
* Docker
* Spring Boot
* Spring Cloud
* Postgres Database

## Application
The application consists of the following microservices
1. Config server
2. Licensing service

## Run
### Prerequisites
* JDK 11
* Maven
* Docker
* Docker Compose

### Docker Compose
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
1. Access the services
    1. licensing service at `http://localhost:8080`
    1. Config service at `http://localhost:8888`
    1. Postgres database at `localhost:5432`
    1. Gitea git server at `http://localhost:3000`
    1. Gitea git server postgres database at `http://localhost:5433`
