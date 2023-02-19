# Art Gallery

## Stack

![](https://img.shields.io/badge/Java_11-✓-blue.svg)
![](https://img.shields.io/badge/Spring_Boot-✓-green.svg)
![](https://img.shields.io/badge/Spring_Security-✓-green.svg)
![](https://img.shields.io/badge/Spring_Profiles-✓-green.svg)
![](https://img.shields.io/badge/Spring_JPA-✓-green.svg)
![](https://img.shields.io/badge/Spring_WebClient-✓-green.svg)
![](https://img.shields.io/badge/Maven-✓-blue.svg)
![](https://img.shields.io/badge/MySQL-✓-blue.svg)
![](https://img.shields.io/badge/Docker-✓-blue.svg)
![](https://img.shields.io/badge/h2-✓-blue.svg)
![](https://img.shields.io/badge/Logbook-✓-blue.svg)
![](https://img.shields.io/badge/Logback-✓-blue.svg)
![](https://img.shields.io/badge/JWT-✓-blue.svg)
![](https://img.shields.io/badge/Lombok-✓-blue.svg)
![](https://img.shields.io/badge/Swagger_2-✓-blue.svg)
![](https://img.shields.io/badge/Passay-✓-blue.svg)
![](https://img.shields.io/badge/JUnit-✓-yellow.svg)
![](https://img.shields.io/badge/AssertJ-✓-yellow.svg)
![](https://img.shields.io/badge/Mockito-✓-yellow.svg)
![](https://img.shields.io/badge/MockMVC-✓-yellow.svg)
![](https://img.shields.io/badge/Spring_MVC_test-✓-yellow.svg)
![](https://img.shields.io/badge/H2-✓-yellow.svg)


## Description

This is a fun project for implementing a REST API that communicates with the API of the Art Institute of Chicago (ARTIC).
More information can be found below and in the Swagger documentation.

## How to use the App

### From docker

1. Make sure you have [Docker](https://docs.docker.com/get-docker/), [Java](https://www.java.com/download/), and [Maven](https://maven.apache.org) installed


2. Fork this repository and clone it

```
$ git clone https://github.com/<your-user>/art-gallery
```

3. Navigate into the folder

```
$ cd art-gallery
```

4. Build, (re)create, start, and attach containers 

```
$ docker-compose up
```

5. Navigate to `http://localhost:8080/swagger-ui.html` in your browser to check everything is working correctly.



### For locally testing

1.-3. steps same as above (don't forget to import the run configurations from `.run` folder)

4. Install dependencies

```
$ mvn install
```

5. Run the project

```
$ mvn spring-boot:run
```

7. Make a GET request to `/users/me` to check you're not authenticated. You should receive a response with a `403` with an `Access Denied` message since you haven't set your valid JWT token yet

```
$ curl -X GET http://localhost:8080/users/me
```

8. Make a POST request to `/users/signin` with the default admin user we programatically created to get a valid JWT token

```
$ curl -X POST 'http://localhost:8080/users/signin?username=admin&password=admin'
```

9. Add the JWT token as a Header parameter and make the initial GET request to `/users/me` again

```
$ curl -X GET http://localhost:8080/users/me -H 'Authorization: Bearer <INSERT TOKEN HERE>'
```

10. And that's it, congrats! You should get a similar response to this one, meaning that you're now authenticated, and you can use the gallery endpoints

```javascript
{
  "id": 1,
  "username": "user1@email.com",
  "roles": [
    "ROLE_ADMIN"
  ]
}
```

## Author

Karoly Jugovits

> Reach out to me directly at [jugovitskaroly@gmail.com]()
