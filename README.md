# School Management System API

A Java 21 / Spring Boot REST API designed to plug into a separate Vue.js frontend.

## Included in this starter
- Student CRUD endpoints
- Request validation and consistent error responses
- Search by student name or admission number
- Pagination
- MySQL configuration for normal use
- H2 in-memory profile for a quick first run
- Swagger UI / OpenAPI documentation
- CORS configured for Vue development servers
- Basic integration tests

## Run in IntelliJ IDEA
1. Extract the ZIP.
2. In IntelliJ, choose **File → Open** and select this folder (the folder containing `pom.xml`).
3. Choose JDK 21 if installed. Allow IntelliJ to import Maven dependencies.
4. Run `SchoolManagementApplication`.
5. The default `dev` profile uses H2 so you can try the API without installing MySQL.

API: `http://localhost:8080/api/v1/students`  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`  
H2 console (dev profile): `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:schooldb` (user `sa`, blank password)

## Use MySQL
Create a database named `school_management`, then set environment variables:
- `DB_URL=jdbc:mysql://localhost:3306/school_management`
- `DB_USERNAME=your_mysql_username`
- `DB_PASSWORD=your_mysql_password`

Run with the `mysql` profile, for example:
`SPRING_PROFILES_ACTIVE=mysql`

Never commit real database credentials to GitHub.

## Endpoints
- `GET /api/v1/students?page=0&size=10&sortBy=lastName&direction=asc`
- `GET /api/v1/students?search=ann`
- `GET /api/v1/students/{id}`
- `POST /api/v1/students`
- `PUT /api/v1/students/{id}`
- `DELETE /api/v1/students/{id}`

Example POST JSON:
```json
{
  "admissionNumber": "ADM001",
  "firstName": "Amina",
  "lastName": "Otieno",
  "email": "amina@example.com",
  "className": "Grade 6"
}
```

## Vue.js / Axios example
```js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  headers: { "Content-Type": "application/json" }
});

export const getStudents = (page = 0) =>
  api.get("/students", { params: { page, size: 10 } });

export const createStudent = (student) =>
  api.post("/students", student);
```

## Next steps
This is a working starter, not the entire finished school platform. Add authentication/roles, teachers, classes, subjects, grades and fee payments in separate stages.
