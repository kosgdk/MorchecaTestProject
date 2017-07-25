# Test project for Morcheca
Simple web application, allows user to register, log in/out and manage registered users.  
Uses in-memory HSQL database.  
Written with: Java 8, Hibernate, Spring MVC, Spring Security.  
Tests: Junit, Mockito, Selenide, Unitils.  
Runs on embedded Tomcat server.

Log in details:  
name: **admin**  
password: **adminpass**  

### How to build and run
Requirements: Java 8, Maven, Chrome browser (for ui tests)

```
mvn package
```

```
java -jar target/morcheca.jar
```

```
http://localhost:8080/morcheca/
```
