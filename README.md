<h1 align="center"> 
    Dena Platform
</h1>  

<h4 align="center">Open source BAAS platform written in Java based on Spring </h4>

<p align="center">
    <a href="http://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat" alt="license" title=""></a>
    <a href="https://travis-ci.org/dena-platform/Dena"><img src="https://travis-ci.org/dena-platform/Dena.svg?branch=master" alt="Build Status"></a>
</p>
   

## Introduction ##
Dena is an Open Source Backend as a service for mobile and web applications. Dena enables developers to build 
application faster.  

Dena have the following feature:  
-  **Persistence** : Storing, retrieving data and schema management.
-  **User Management**: Provide login, logout and user registration.
-  **App Management**: Creating application.
-  **Security**: Authentication based on JWT protocol.
  



## Getting Started ##
First go to the root folder and then run following command (Java 8 is required)

```
$ ./mvnw.cmd clean spring-boot:run

OR (if you have maven installed)

$ ./mvn clean spring-boot:run
```

Dena by default uses embedded MongoDB database, but you can use your local MongoDB. For configuration please 
see application.properties file in the project.  

By default database is configured with:

``` 
email:admin@dena-platform.com 
password:123456  
 ```
After calling login API you get an JWT token that you can use for calling other web service.
For more information please see 
<a href="https://github.com/dena-platform/Dena/wiki/REST-API">REST-API</a> documentation.

## Road Map ##
-   **Authorization** 
-   **Search** 
-   **Client SDK**
-   **Dashboard**

