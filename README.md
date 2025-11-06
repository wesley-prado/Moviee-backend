# 🎬 Moviee - Movie Reservation System

Movie is a back-end system for a movie reservation service built with Java 21 and Spring Boot.  
The service allows users to sign up, log in, browse movies, reserve seats for specific showtimes,
and manage their reservations.  
The system features user authentication with OAuth2, role-based authorization, movie and session
management, seat reservation functionality, and reporting on reservations.

## Technology Stack

### Core Technologies

[![Java 21](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)](#)  
[![Spring Boot 3.4.6](https://img.shields.io/badge/Spring_Boot-3.4.6-green?logo=springboot&logoColor=white)](#)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?logo=postgresql&logoColor=white)](#)  
[![Docker Compose](https://img.shields.io/badge/Docker_Compose-blue?logo=docker&logoColor=white)](#)  
[![Maven 3.9.9](https://img.shields.io/badge/Maven-3.3-red?logo=apachemaven&logoColor=white)](#)

### Infrastructure and DevOps

[![Docker](https://img.shields.io/badge/Docker-blue?logo=docker&logoColor=white)](#)  
[![nginx](https://img.shields.io/badge/nginx-009639?logo=nginx&logoColor=fff)](#)  
[![Jenkins](https://img.shields.io/badge/Jenkins-D24939?logo=jenkins&logoColor=white)](#)  
[![DigitalOcean](https://img.shields.io/badge/DigitalOcean-0080FF?logo=digitalocean&logoColor=white)](#)

## Project Structure

The project implements a feature-based architecture to promote high cohesion and low coupling.
Business logic domains (like `movie`, `room`, `session`) are encapsulated within the main `cinema`
package. Cross-cutting concerns such as security configuration (`auth`), user data management (
`user`), and infrastructure logic (`core`) are separated into distinct root packages.

```ASCII
src/main/java/com/codemages/Moviee
├── cinema/                # Main business logic
│   ├── movie/             # Domain
│   │   └── assembler      # HATEOAS response helper
│   │   └── constant       # Constants like enums
│   │   └── controller
│   │   └── dto
│   │   └── exception
│   │   └── GenreService
│   │   └── Movie
│   │   └── MovieRepository
│   │   └── MovieService
│   ├── room/
│   ├── session/
│   ├── ticket/
├── auth/                    # Auth configuration and resources
├── user/                    # User management
└── core/                    # Cross-cutting modules
    ├── config/              # General configurations like ObjectMapper
    ├── exception/           # Global exception handling
    └── logging/             # Logging configuration and demonstration endpoint
```

## Features

### User Authentication and Authorization

- Private client OAuth2-based authentication (planning to use public client)
- Role-based access control (ADMIN, MODERATOR, USER)
- Custom login page with username/password authentication
- Custom consent page for OAuth2 authorization flow
- Password validation with strong password requirements (upper, lower, special characters, digits
  and minimum length)
- JWT token-based authentication with refresh-token

### API Documentation

- REST API with HAL (Hypertext Application Language) support
- HATEOAS implementation for API discoverability
- Swagger (to be implemented soon)

### Development and Deployment

- Docker support for containerized deployment
- Docker Compose configuration for orchestration
- Maven wrapper for easier build management
- PostgreSQL support for production
- Test Containers for integration testing

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven (or use the included mvnw wrapper)
- Docker and Docker Compose (optional, for containerized deployment)

### Running the Application

**Using Docker (dev):**

```bash
docker compose -f docker-compose.dev.yml up -d
```

The application will be available at [https://localhost:443](https://localhost:443).

**Note on SSL Certificate:**
Nginx is configured to use SSL for local testing. You must generate a self-signed certificate before
running Docker Compose.

1. Navigate to the `nginx-dev` directory (or appropriate location).
2. Run the following OpenSSL command (if you don't have an existing certificate):

   ```bash
   # Generates a certificate valid for 365 days
   openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
     -keyout nginx-selfsigned.key -out nginx-selfsigned.crt \
     -subj "/C=BR/ST=Sao Paulo/L=Sao Paulo/O=Dev/CN=localhost"
   ```

3. After generating, start the application. You may need to accept the browser's security warning
   upon first access.

### Default Credentials

* **Admin user:**
    * Username: `admin`
    * Password: `Admin1#@`

* **Regular user:**
    * Username: `myuser`
    * Password: `User1#@@`

## Notes on Login and Consent Pages

Although the primary goal of this project's API component (Resource Server) is to function as a
backend system, this application currently implements a monolithic structure that also includes the
role of an Authorization Server.

For learning purposes and to demonstrate the complete OAuth2 flow using Spring Authorization Server,
custom login (`/login`) and consent (`/oauth2/consent`) pages are included. These pages are
essential for user-interaction flows (like Authorization Code Grant), allowing the user to
authenticate and grant permissions.

**Future Architectural Improvements:**

A potential enhancement would be to separate this monolith into distinct services:

1. **Authorization Server:** This service would retain the login and consent pages, focusing
   exclusively on authentication and token issuance.
2. **Resource Server:** This service would become a pure backend API, containing only the business
   logic endpoints, and would be configured to validate tokens issued by the separate Authorization
   Server.

This separation reflects a more robust decoupled architecture, where the Resource Server itself
contains no user interface elements.

## License

This project is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
