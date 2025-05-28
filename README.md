# LoginResgistrationSystem

This app uses Layered Architecture
________________________________________
Main Features:
1) Registration
2) email verification
3) login
4) JWT authentication
________________________________________
- User API --> Spring Web 
- Database (Users entity)	--> Spring Data JPA + PostgreSQL 
- Authentication & Security --> Spring Security + JWT 
- Password hashing --> via Spring Security (typically using BCrypt)
- Email verification --> Spring Mail 
- Input validation --> Spring Validation 
- Rapid development	--> DevTools & Lombok 
- Testing	--> JUnit & Spring Boot Testing
________________________________________
# First Phase
Infrastructure Setup:
1) gradle file: include dependencies of the needed technology
2) .env: for system variables
3) application.properties 
4) setup PostgreSQL

 
