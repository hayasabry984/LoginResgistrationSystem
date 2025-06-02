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
# First Phase: Infrastructure Setup:
1) gradle file: include dependencies of the needed technology
2) .env: for system variables
3) application.properties 
4) setup PostgreSQL

# Second Phase: Database Layer
1) users entity/model classes: this represents the tables structure in the database, so we will have one class 'users'
2) user repository interface: it handles database operations on users table
3) DTOs: Data Transfer Objects to hold data sent from frontend, so we will have DTOs for registration, email verification,login

# Third Phase: Data objects sent to & from the frontend 
1) DTOs: Data Transfer Objects to hold data sent from frontend, so we will have DTOs for registration, email verification,login
2) LoginResponse: return JWT and expiration time after a successful login for security

# Fourth Phase: Configuration
1) ApplicationConfiguration: To allow the login and registration so we have to configure the spring security's authentication mechanism
2) JwtAuthenticationFilter: we have to build this so spring security can use it with each request to validate the token and authenticate user
3) SecurityConfiguration: we need to configure spring security to use JWT, allow requests from frontend, and protect API endpoints

# fifth Phase: Service
1) JwtService: it creates, signs, extracts, validates for security