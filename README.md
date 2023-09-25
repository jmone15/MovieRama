# MovieRama

The application is (yet another) social sharing platform where users can share their favorite movies.

##### Technology Stack

###### Backend

* Java 11
* Spring Boot 2.7.15
* PostgreSQL 14.2
* Maven 3.8.4

###### Frontend

* React 18.2.0
* node v14.17.4
* npm 6.14.14

##### Installation Instructions

- In a terminal, make sure you are inside `MovieRama` root folder

- Run the following command to start docker compose containers
  ```
  docker compose up -d
  ```

- **movierama-api**

    - Open a terminal and navigate to `MovieRama/movierama-api` folder

    - Run the following `Maven` command to start the application
      ```
      ./mvnw clean spring-boot:run
      ```

- **movierama-ui**

    - Open another terminal and navigate to `MovieRama/movierama-ui` folder

    - Run the command below if you are running the application for the first time
      ```
      npm install
      ```

    - Run the `npm` command below to start the application
      ```
      npm start
      ```
 - **Importing data into the database**
 
   In the directory `movierama-api/src/main/resources/` you can find the `data.sql` file which includes a collection of data that can be imported in the backend for testing purposes.
   In order to trigger the data import to to the `application.yml` and change the following property to `always`:

    ```
    spring:
       sql:
        init:
          mode: always #initially set to never run
    ```
   You can also set the property to `always` before starting the backend for the first time. It will proceed to import the data after the creation of the entities (Which is automatically executed on startup).

   **Note**: After running the application with the import script enabled please proceed to set the property again to `never` to avoid having issues after re-running the `movierama-api`.
    
### movierama-api

### ER Diagram

<p align="center">
  <img src="https://github.com/jmone15/MovieRama/blob/main/db-diagram/ER-diagram.png" alt="Sublime's custom image"/>
</p>

`Spring Boot` Web Java backend application that exposes a Rest API to create, retrieve and delete movies. If a user
has `ADMIN` role he/she can also retrieve information of other users or delete them. The application secured endpoints
can just be accessed if a valid JWT access token is provided.

In order to get the JWT access token, the user can log in using the credentials (`username` and `password`) created when
he/she signed up directly to the application.

`movierama-api` stores its data in [`Postgres`](https://www.postgresql.org/) database.

### movierama-ui

`React` frontend application where a user with role `USER` can add movie entries and share their opinion about other
movies by voting (like or hate). On the other hand, a user with role `ADMIN` has access to user management endpoints.

In order to access the application, a `user` or `admin` can log in using the credentials (`username` and `password`)
created when he/she signed up directly to the application. All the requests coming from `movierama-ui` to secured
endpoints in `movierama-api` have the JWT access token. This token is generated when the `user` or `admin` logins.

`movierama-ui` uses [`Semantic UI React`](https://react.semantic-ui.com/) as CSS-styled framework.

