

## Introduction

Book Shop is a platform for passionate book lovers...
It offers a user-friendly interface for browsing, purchasing, and managing a personal library. 
With features like shopping carts and user account management, the platform ensures a seamless shopping experience. 
The project also includes role-based access control, allowing administrators to manage inventory and users effectively.


## Technologies Used

* **Programming Language:** Java
* **Application Configuration:** Spring Boot, Spring, Lombok, Mapstruct, Maven
* **Accessing Data:** Spring Data JPA, Hibernate, MySQL
* **Security:** Spring Security, JWT
* **Web Development:** Spring MVC, Tomcat
* **Database Migration:** Liquibase
* **Containerization:** Docker
* **Testing:** JUnit, Mockito, Test Containers
* **API Documentation:** Swagger
* **Version Control:** Git

## Installation

### Prerequisites

- Java 11 or higher
- Maven
- MySQL


### Steps

1. Clone the repository:
   `bash git clone https://github.com/AndrewOliferchuk/Book-Store-Api`

2. Navigate to the project directory:`bash cd Book-Store-Api`

3. Create a MySQL database:`sql
   CREATE DATABASE name`, (instead of `name` you can put your database name);

4. Update the `env.template` file with your MySQL database credentials, also
   you can use `resources/application.properties` for your MySQL database credentials;

5. Build the project using Maven:
   `bash
   mvn clean package
   `

6. Run the application:
   `bash
   mvn spring-boot:run
   `


### Docker Initialization Issue

When setting up Docker, I encountered an issue where executing docker-compose up resulted in my project endlessly attempting to start. 
The root cause was that MySQL took longer to initialize compared to the application. 
This led to numerous errors because the application tried to connect to the database before it was ready.

### Solution
To avoid this problem during the first build, it’s advisable to start the database first and then launch the application. 
Additionally, it's essential to verify that Liquibase is functioning correctly. 
Issues like mismatched column types and other discrepancies can cause database formation problems.


### User Controllers

AuthenticationController: Handles user registration and login.
BookController: Allows users to view, create, update, and delete books.
CategoryController: Manages categories and retrieves books by category.
OrderController: Handles creating orders and viewing order details.

### Admin Controllers
BookController: Allows administrators to create, update, and delete books.
CategoryController: Enables administrators to manage categories.
OrderController: Allows administrators to update order statuses and manage orders.
ShoppingCartController: Manages the shopping cart for adding, updating, and removing items.


## Contact

For any questions or suggestions, feel free to reach out:

- GitHub: [AndrewOliferchuk](https://github.com/AndrewOliferchuk)
- email: andrewshop4.o@gmail.com
