# Config Server
Configuration server using the Spring Cloud Config Server

## Introduction
The config server provides a central place to provide configurations to other microservices.
It uses the Spring Cloud Config Server application to achieve this. Therefore, there is no code in this module.
The configurations, themselves, are not baked inside the application. They can be fetched from several different
sources.

[See Spring Cloud Config reference](https://cloud.spring.io/spring-cloud-config/reference/html/)


## Configuration sources
Currently, two sources of configuration have been implemented:

1. File System
1. Git repository

### File System
The file system configuration source is activated when the application is run with the `native` spring profile.
It should only be used for testing locally. Any change in the configuration files require the application to be rebuit.

### Git Repository
The git repository source is activated when the application is run with the `docker` spring profile. The repository
pointed to is the `config-repo` repository hosted on the gitea server than runs with the provided docker-compose.

**NOTE** that you need to setup this repository yourself when you run the docker-compose for the first time.
Due to absence of repository, the config server will crash and will need to be restarted. It can be done using
`docker-compose start`. The gitea server and the corresponding database server are mounted on docker volumes so the
repository will be available even after the containers are destroyed. To remove the volumes you can run
`docker volume prune` after deleting the docker-compose stack (`docker-compose down`).


## Encryption and Decryption
### Encryption
Passwords and secrets in the configuration should not be stored in raw form in the configuration files.
The should only be stored in an encrypted form.

The config server provides two endpoints `/encrypt` and `/decrypt` that you can use to encrypt your secrets before
writing them in a file. They are POST requests and the data to encrypt is pass in the body.
To use encryption, the `ENCRYPT_KEY` environment variable must have been set before
the application launched.

Example requests:
* `curl http://localhost:8888/encrypt -d my-secret-password`
* `curl http://localhost:8888/decrypt -d MY_ENCRYPTED_PASSWORD`

### Decryption
To decrypt the secrets on the client side, the same `ENCRYPT_KEY` environment variable must also be available
on the client. The client application also needs the following dependency:
```
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-rsa</artifactId>
</dependency>
```

## Security
The config server has not implemented any security features yet.
Authenticated http requests is one feature that can be implemented.


[See Spring Cloud Config reference]: https://cloud.spring.io/spring-cloud-config/reference/html/