# 🔐Login & Registration System with Spring Boot, JWT, and Email Verification

A robust and secure user authentication system built with Java 22, Spring Boot 3, Spring Security, JWT (JSON Web Tokens), PostgreSQL, and Gmail SMTP for email verification.

This project demonstrates a production-ready authentication system using modern Spring Boot practices, making it ideal for real-world applications.

---
## ✅ Features
- 📬 **User Registration** with email & password
- 🔐 **BCrypt Password Hashing** for secure credential storage
- 📧 **Email Verification** with expiring 6-digit code
- 🔑 **JWT-Based Login** for stateless, secure sessions
- 🔄 **Resend Verification Code** if the user missed the email
- 👤 **Get Current User** and list all users (protected endpoints)
- 🚫 Proper error handling & account access control
- ⚙️ CORS configuration for frontend integration (e.g., React, Angular)
---
## 🛠️ Technologies Used
| Stack | Tech |
|-------|------|
| Language | Java 22 |
| Framework | Spring Boot 3.4+ |
| Security | Spring Security, JWT, BCrypt |
| Database | PostgreSQL |
| Mail | Gmail SMTP via Spring Mail |
| Build Tool | Maven |
| API Testing | Postman |
---
## 📁 Project Structure

src/

├── configuration/ # Security, JWT, Email, CORS configs

├── controller/ # REST endpoints

├── dto/ # Data Transfer Objects

├── model/ # JPA Entity

├── repository/ # Spring Data JPA Repository

├── response/ # Login response wrapper

├── service/ # Business logic (Auth, JWT, Mail, User)

└── Main.java # Spring Boot entry point

---
## 🔍 Postman API Testing
🚀 Base URL: http://localhost:8080

| Endpoint                 | Method | Description                    |
| ------------------------ | ------ | ------------------------------ |
| `/auth/signup`           | `POST` | Register a new user            |
| `/auth/verify`           | `POST` | Verify email with code         |
| `/auth/login`            | `POST` | Authenticate and get JWT       |
| `/auth/resend?email=...` | `POST` | Resend verification code       |
| `/users/me`              | `GET`  | Get current authenticated user |
| `/users/`                | `GET`  | Get list of all users          |
---
## 🛡️ Security Notes
All passwords are hashed with BCrypt

JWT tokens are signed with a secret key

Email verification codes expire in 15 minutes

Protected endpoints require valid JWT tokens

CORS allows frontend apps on http://localhost:8080

---
## UseCase Diagram
![LoginRegistration_UseCaseDiagram drawio](https://github.com/user-attachments/assets/116cbcb0-fe2b-4c7f-b865-89eeea0cafc2)


---
## Class Diagram
![Login Registration drawio](https://github.com/user-attachments/assets/2cca754d-999b-4104-bbc9-6057fe8ad80c)

---
## Sequence Diagram 1: User Registration (Sign Up) and Email Verification
![image](https://github.com/user-attachments/assets/ea9743a4-6fdc-46e3-a2e4-70f5c0fced9d)

---
## Sequence Diagram 2: Resend Verification Code
![image](https://github.com/user-attachments/assets/244af59e-0c1e-4d35-9e03-2ad6562de6c4)

---
## Sequence Diagram 3: User Login (Authentication)
![image](https://github.com/user-attachments/assets/7d138e95-8e4a-461c-9ff2-cd34de5747ec)

---
## Sequence Diagram 4: Access Protected Endpoint (JWT Validation)
![image](https://github.com/user-attachments/assets/48339bf4-acf5-4ab5-909f-c9f8738b5793)

---
## Sequence Diagram 5: Get Current User
![image](https://github.com/user-attachments/assets/3d1e990d-3419-44ef-9ea3-309c8a864d5a)

---
## Sequence Diagram 6: Get All Users
![image](https://github.com/user-attachments/assets/b1ba17b7-9e76-45ee-8e60-216efe21daed)
