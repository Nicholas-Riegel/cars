# TODO - Cars Gallery Project

## ✅ Completed
- [x] Spring Boot backend setup
- [x] Docker containerization working
- [x] Docker Compose configuration
- [x] Local development environment

## 🚀 Phase 1: Get Online ASAP (Priority)
- [x] Create GitHub repository
- [x] Push current code to GitHub
- [x] Set up Hostinger VPS
- [x] Create Docker Hub account
- [x] Create one new Docker Hub repo called cars-backend
- [x] Push cars-backend to DH repo
    ```bash
    docker build -t yourusername/cars-gallery-backend:latest .
    docker images
    docker login
    docker push yourusername/cars-gallery-backend:latest
    ```
- [x] Create and add .github/workflows/build-and-push.yml
- [x] Add secrets to the github repo for DOCKER_HUB_USERNAME and DOCKER_HUB_ACCESS_TOKEN for yml file
- [x] Create ssh keys
- [x] Add them to ~/.ssh
- [x] Add public key to VPS: nano ~/.ssh/authorized_keys
- [ ] Create basic GitHub Actions CI/CD pipeline
- [ ] Deploy current "Hello World" backend online
- [ ] Verify live endpoint works at your domain

## 🔧 Phase 2: Basic Backend Features
- [ ] Add simple REST endpoint (`/api/hello`)
- [ ] Add health check endpoint (`/actuator/health`)
- [ ] Test endpoints work locally and online
- [ ] Add basic error handling

## 🗄️ Phase 3: Database Integration
- [ ] Add PostgreSQL to docker-compose
- [ ] Configure Spring Data JPA
- [ ] Create Car entity and repository
- [ ] Add database migrations with Flyway
- [ ] Create basic CRUD endpoints for cars

## 🖼️ Phase 4: Image Handling
- [ ] Add file upload endpoint
- [ ] Configure image storage (local files)
- [ ] Add image serving endpoint
- [ ] Test image upload/display

## 💬 Phase 5: Comments System
- [ ] Create Comment entity
- [ ] Add comment CRUD endpoints
- [ ] Enable anonymous comments
- [ ] Basic comment validation

## 🎨 Phase 6: Frontend (React)
- [ ] Set up React + TypeScript project
- [ ] Create basic car gallery UI
- [ ] Connect to backend API
- [ ] Add Tailwind CSS styling
- [ ] Deploy frontend with backend

## 🔄 Phase 7: CI/CD Optimization
- [ ] Optimize Docker images for production
- [ ] Add automated testing
- [ ] Set up staging environment
- [ ] Add monitoring and logging

---
**Current Focus:** Phase 1 - Get the basic app online with CI/CD