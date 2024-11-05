<a id="idtext"></a> 
# Online Commerce Application - Backend


<div align="center">
<br>
A Spring Boot-based shopping application with JWT authentication, user management, and shopping cart functionality.
<br/><br/>
</div>

### Introduction

The Shopping Project is a Spring Boot web application designed to provide a user-friendly online shopping experience. It features secure user authentication with JWT and Spring Security, enabling reliable access control and user management. 
Users can register, log in and manage their passwords. An administrative interface allows administrators to add products and manage product details efficiently.
Key functionalities include a shopping cart where users can add products, adjust quantities, and prepare items for checkout. The application also includes product image zoom feature, enhancing the browsing experience with detailed product visuals.
<br/><br/>

## :star: Key Features

1. **User Authentication**
   
   - Secure authentication with JWT and Spring Security, allowing users to register, log in, and manage passwords safely.

2. **Admin Dashboard**
   - Add new products and update product information through a admin interface.
  
3. **Shopping Cart**
    - Users can add products to their cart, and prepare items for checkout.

4. **Product Image Management**
   - Allows administrators to upload product images.
   - Users can zoom in on product images on the product detail page.
     
5. **Database Integration**
   - Use MySQL for data storage and JPA repositories for database operations.
  
6. **RESTful API**
   - Provides endpoints for user authentication, cart management, and image handling.
  <br/>
  
## :wrench: Built With
  - Base : JAVA
  - Frameworks : Spring Boot
  - Build Tool: Maven
  - Database : MySQL
  - Security : Spring Security , JWT (JSON Web Tokens)
  - Libraries: Lombok
  - JPA (Java Persistence API) : Spring Boot Starter Data JPA
  - API Testing: POSTMAN
<br/>

## Getting Started
To get a local copy up and running, follow these steps:

#### Prerequisites
> [!IMPORTANT]
> Ensure you have the following installed on your machine:
- Java (17 or 17+)
- Maven
- MySQL
<br/>

#### Installation 

1. Clone the repository
```sh
   git clone https://github.com/DanielYCheon/app-commerce-system.git
```
2. Navigate to the project directory
 ```sh
    cd app-commerce-system
 ```
3. Set up the Database
   
   a. Ensure MySQL is installed and running.
   
   b. Create a Database named shoppingproject
   
   c. Upload the database configuration in **_src/main/resources/appliaction.properties_**

      ```ini
      spring.application.name=shoppingProject
      spring.jpa.hibernate.ddl-auto=update
      spring.datasource.url=jdbc:mysql://localhost:3306/shoppingproject
      spring.datasource.username=root
      spring.datasource.password=yourpassword
      ```
4. Build the project
   ```sh
   mvn clean install
   ```
5. Run Application
   ```sh
   mvn spring-boot:run
   ```
<br/>

## :rocket: Usage

> [!NOTE]
> The application will start on "http://localhost:8080" 



### API Endpoints

   - **Register User**
     
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
"username": "user1",
"email": "user1@example.com",
"password": "password"
}

```
<br/>

   - **Authenticate User (Login)**

```http
POST http://localhost:8080/api/auth/authenticate
Content-Type: application/json

{
"username": "user1",
"password": "password"
}
 ```
<br/>

   - **Refresh Token**

```http
POST http://localhost:8080/api/auth/refresh
Content-Type: application/json

{
"refreshToken": "your_refresh_token"
}
```
<br/>

- **Check Token**

```http
GET http://localhost:8080/api/auth/checkToken
Content-Type: application/json

{
"Access Token": "your_access_token"
}
```
<br/>

- **Change Password**
  
```http
POST http://localhost:8080/api/auth/change-password
Content-Type: application/json

{
"username": "user1",
"oldPassword": "old_password",
"newPassword": "new_password"
}
```
<br/>

 - **Add to Cart**
```http
POST http://localhost:8080/api/auth/add
Content-Type: application/json
{
"username": "USER NAME",
"productName": "PRODUCT NAME",
"quantity": 1
}
```
<br/>

 - **Upload Image**
 
```http
POST http://localhost:8080/api/auth/imageupload
Content-Type: multipart/form-data
{
"image": "file",
"productName": "product1",
"productDescription": "description",
"productPrice": "price",
"gender": "gender"
}
```
<br/>

- **Download Image:**

```http
GET http://localhost:8080/api/auth/image/{imageName}
```
<br/>

- **Download Image Details:**
  
```http
GET http://localhost:8080/api/auth/image/detail/{imageName}
```
<br/>

- **Get Products by Gender:**
  
```http
GET http://localhost:8080/api/auth/get/products/{gender}
```
<br/>

### Security

- The application uses JWT for authentication.
- Update the JWT secret in `src/main/resources/application.properties`

```ini
spring.jwt.secret=your_secret_key
```
<br/>

### Dependencies

- Spring Boot Starter Data JPA
- Spring Boot Starter JDBC
- Spring Boot Starter Security
- Spring Boot Starter Web
- MySQL Connector
- Lombok
- JJWT

For more details, refer to the `pom.xml` file.
<br/><br/>

## :link: References

   - [Spring Security Documentation](https://docs.spring.io/spring-security/reference/getting-spring-security.html) – The resources for understanding Spring Security, including authentication and authorization.
   - [JWT Introduction](https://jwt.io/introduction) – Understanding JWT tokens and how they are processed in user authentication.
   - [Auth0 Community – JWT](https://community.auth0.com/c/jwt/8) – Community-driven solutions and discussions for resolving JWT-related issues.
   - [Spring Security – OAuth2 Resource Server with JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html) – Guide on configuring Spring Security as an OAuth2 resource server with JWT support.


<br/><br/>

<div align="right">
   
   [Go To Top](#idtext)
   
</div>



## :pencil: License

This project is licensed under the MIT License - see the LICENSE file for details.


     
 


