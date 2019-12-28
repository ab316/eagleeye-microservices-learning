# Authentication Service
OAuth2 Authentication server

## Introduction
The authentication service is actually an OAuth2 authorization server that authenticates users and provides them
with JSON Web Tokens (JWTs) that can be used to call the domain services.

### Current Implementation
At this point the client id and secret, along with the available users are hardcoded in the coded.
The JWT signing key is fetched fro the configuration server.

The implementation is also based on the deprecated Spring Security OAuth project.


## Usage
See the README at the repository root for instructions

## Future Work
In this future, this project should implement the first class OAuth2 support in the Spring Security project.
Another option is that this service is completely discarded in favour of an off-the-shelf OAuth2 authorization
server.
