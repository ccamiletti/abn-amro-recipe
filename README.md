# [ABN AMRO Recipe APP using Java and Spring boot]


> ### Spring boot codebase containing abn-amro-recipe app (CRUD, Http Basic Auth, Hibernate, Liquibase, docker, etc.) that adheres to the abm-amro assignment spec.

This codebase was created to demonstrate a backend implementation built with Spring boot + Hibernate including CRUD operations, authentication, and more.
# How it works

## Recipe api
The application abn-amro-recipe uses Spring boot (Rest + Hibernate).

* Use the idea of Domain Driven Design to separate the business term and infrastructure term.
* Use Hibernate to implement persistence.

In general terms, the structure of the project is organized as follows::

1. `Controller` The request goes to the controller, and the controller maps that request and handles it. After that, it calls the service logic if required.
2. `Service` Handles all the business logic. It consists of service classes and uses services provided by data access layers
3. `Repository` Contains all the storage logic and translates business objects from and to database rows.
4. `config`  contains all the configuration classes.

# Security

Basic Authentication with Spring Security was implemented

# Database
It uses a Mysql database, it is created and executed in a Docker container.

# Liquibase
Liquibase was implemented to create and maintain the database schema 

# Getting started

Let's open a terminal, go to app_directory/docker and run `docker-compose up -d && docker-compose logs -f` (logs -f to see the log on order-consumer application)

# Api Documentation

To see the documentation api, open a browser tab at http://localhost:1234/api/swagger-ui.html. then, Click on: `recipe-controller`

# Try it out with Postman
To start using the API, first of all, we have to create a user. To do so, call this endpoint:

POST:/ 
`http://localhost:1234/api/user`, 
`` whit Body``
```
 1. {
 2.  "user":"some_user_name",
 3.  "password":"your_password" 
 4. }
```
After the user was created, you have to use it in each postman call (just add it in: Authorization, type: Basic Auth)

The entry point address of the API (recipe) is at http://localhost:1234/api/recipe

# Access to the Data Base.
Do you want to get access to the database?. please, open your browser and go to: http://localhost:8888/
```
System: MySQL
Server: mysql-server
Username: admin
Password: admin
Database: abn_amro_db
```
# Help
Please fork and PR to improve the code.