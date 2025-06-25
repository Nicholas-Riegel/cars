# Cars Gallery Project

## Project Overview

A car gallery application where users can view car photos and leave comments. Built as a full-stack application with a modern tech stack, containerized for easy deployment.

This project is also a learning project. So I don't want the AI agent to write code or make changes unless explicitly authorized to do so. I may ask the AI agent questions, and I would like responses, but I do not want the AI agent to write code or automatically make changes. 

I would like to try to follow CI/CD methodology. I would like to get the app hosted on hostinger asap and continue to build it there.

## Technology Stack

### Backend
- **Java 17+** with **Spring Boot 3.x**
- **Spring Data JPA** for database operations
- **Spring Web** for REST API
- **Flyway** for database migrations
- **Maven** for dependency management

### Frontend
- **React 18+** with **TypeScript**
- **Vite** for build tooling
- **Axios** for API communication
- **React Router** for navigation
- **Tailwind CSS** for styling

### Database
- **PostgreSQL 15+**
- Optimized for image metadata and comments

### Infrastructure
- **Docker** and **Docker Compose** for containerization
- **Hostinger VPS** for deployment
- **Nginx** as reverse proxy (in Docker)

### Image Storage (Recommendations)
- **Option 1**: Local file system with Docker volumes (simple, cost-effective)

### Basic File Structure so far
cars/
├─ backend/
│  ├─ src/...
│  ├─ pom.xml
│  ├─ Dockerfile       # Backend image instructions
├─ frontend/
│  ├─ src/...
│  ├─ package.json
│  ├─ vite.config.js
│  ├─ Dockerfile       # Frontend image instructions
├─ nginx/
│  ├─ default.conf     # Nginx config file
├─ docker-compose.yml  # Compose file for all services
