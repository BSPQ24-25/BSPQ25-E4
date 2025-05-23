# 🚗 CarRentalSystem
# BSPQ25-E4


## 📁 GitHub repository: [https://github.com/BSPQ24-25/BSPQ25-E4](https://github.com/BSPQ24-25/BSPQ25-E4)


Java-based car rental management system developed as part of a university project.  
Includes database integration, unit testing, and code coverage.

---

## 📦 Technologies Used

- **Java 17**
- **Maven**
- **MySQL** (via JDBC)
- **JUnit 5** (unit testing)
- **Mockito** (mocking framework)
- **JaCoCo** (code coverage)
- **Git** (version control)

---

## ⚙️ Project Setup
To set up the database, import db/carrental.sql into MySQL.
Configure your src/main/resources/config.properties (DO NOT PUSH YOURS)

To run the tests go to the projects directory and **mvn test**, and to generate coverage report with JaCoCo: **mvn jacoco:report**.
Output: target/site/jacoco/index.html

To run the project:

1. mvn clean compile
2. mvn package (optional)
3. mvn spring-boot:run
4. Go to http://localhost:port on browser
5. Admin Credentials:  
   a. Email: admin2@example.com  
   b. Password: admin2
6. User Credentials:  
   a. Email: user5@example.com  
   b. Password: hola11

## ▶️ Ejecutar el proyecto

Para compilar y ejecutar la aplicación, usa los siguientes comandos:

```bash
# Clone the repository
git clone https://github.com/BSPQ24-25/BSPQ25-E4.git
cd BSPQ25-E4

# Run the app
mvn clean spring-boot:run

# Run the tests
mvn clean test

