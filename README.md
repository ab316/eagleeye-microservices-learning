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

### Procedure
1. Build the project `mvn clean install`
1. Run docker-compose `docker-compose -f docker/docker-compose.yml up -d`
1. Create an empty git repository called *config-repo*
1. Copy the folder *licensing-service* from *config-server/src/main/resource/config*
to the new git repository
1. `git remote add origin http://localhost:3000/abdullah/config-repo.git`
1. `git push -u origin master`
1. Access the services
    1. licensing service at `http://localhost:8080`
    1. Config service at `http://localhost:8888`
    1. Postgres database at `localhost:5432`
    1. Gitea git server at `http://localhost:3000`
    1. Gitea git server postgres database at `http://localhost:5433`
