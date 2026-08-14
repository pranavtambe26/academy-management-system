# 🏏 Cricket Academy Management System

A full-featured, production-deployed backend system for managing a cricket academy — built end-to-end with Spring Boot, Oracle, Docker, and a real cross-cloud AWS + OCI deployment pipeline.

**Live API Docs:** [http://3.109.107.211:8080/swagger-ui/index.html](http://3.109.107.211:8080/swagger-ui/index.html)

---

## Overview

This system manages the full lifecycle of a cricket academy: student and coach onboarding via an admin-approval workflow, batch assignment, attendance tracking with real business-rule enforcement, performance ratings, and a two-directional feedback system with role-based visibility controls.

It was built as a complete, real-world backend engineering project — covering domain modeling, authentication/authorization, testing, containerization, and cloud deployment — rather than a tutorial-scale CRUD app.

## Architecture

```
┌─────────────┐      HTTPS       ┌──────────────────────┐      mTLS (Wallet)      ┌───────────────────────┐
│   Client    │ ───────────────► │  Spring Boot 4.1 App │ ──────────────────────► │  Oracle Autonomous DB │
│ (Postman /  │                  │  Docker container on │                          │  (OCI, Always Free)    │
│  Swagger UI)│ ◄─────────────── │  AWS EC2 (Mumbai)     │ ◄────────────────────── │                        │
└─────────────┘                  └──────────────────────┘                          └───────────────────────┘
                                          │
                                   GitHub Actions CI/CD
                                  (build → test → deploy
                                   on every push to main)
```

**Why cross-cloud (AWS compute + OCI database)?** Self-hosting Oracle in a container requires more RAM than AWS's free-tier compute instances provide. OCI's Always Free Autonomous Database offers a genuinely free, fully-managed Oracle instance with no time limit — a deliberate, cost-conscious architectural choice rather than paying for a larger compute instance solely to run a database.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (Spring Framework 7) |
| Security | Spring Security 7 + JWT (stateless auth) |
| Persistence | Spring Data JPA / Hibernate 7 |
| Database | Oracle Database (19c, OCI Autonomous DB) |
| Migrations | Flyway |
| Testing | JUnit 5 + Mockito |
| API Docs | Springdoc OpenAPI (Swagger UI) |
| Containerization | Docker + Docker Compose |
| Cloud | AWS EC2 (compute) + OCI Autonomous Database (data) |
| CI/CD | GitHub Actions |

## Core Features

- **JWT-based authentication** with role-based access control (`ADMIN`, `COACH`, `STUDENT`)
- **Admin-approval onboarding workflow** — coaches and students submit requests; admin reviews and approves/rejects
- **Batch management** — many-to-many Coach↔Batch and Student↔Batch relationships via explicit join entities
- **Attendance tracking** — enforces that a coach can only mark attendance for batches they're assigned to, verified at the service layer
- **Performance ratings** — coach-to-student ratings with batch-scoped authorization
- **Two-directional feedback system:**
  - Student → Coach: anonymous to the coach, fully visible to admin only
  - Coach → Student: visibility toggle (`STUDENT_VISIBLE` / `ADMIN_ONLY`) enforced at the database query level
- **Global exception handling** with consistent, predictable error responses
- **Automated tests** covering both standard CRUD logic and authorization-critical business rules

## Security Highlights

- Passwords hashed with BCrypt, never stored or logged in plain text
- Stateless JWT authentication — no server-side session state
- Method-level authorization (`@PreAuthorize`) layered on top of URL-level rules
- IDOR protection — "my data" endpoints resolve identity from the verified JWT, never from a client-supplied ID
- Secrets externalized via environment variables — never committed to source control
- Database connection secured via Oracle Wallet (mTLS), not a plain host:port JDBC URL

## Running Locally

**Prerequisites:** Docker Desktop, Docker Compose

```bash
git clone https://github.com/pranavtambe26/academy-management-system.git
cd academy-management-system
docker compose up -d
```

This starts the Spring Boot application alongside a self-hosted Oracle XE container. The API will be available at `http://localhost:8080`, with interactive docs at `http://localhost:8080/swagger-ui/index.html`.

## CI/CD Pipeline

Every push to `main` triggers a GitHub Actions workflow that:
1. Builds the project and runs the full test suite
2. Only on success, automatically deploys the latest code to the live AWS EC2 server via SSH, rebuilding and restarting the containerized application

## Project Status

This backend is complete and deployed. Current focus areas:
- [ ] Web frontend (React)
- [ ] Mobile app (planned)
- [ ] Analytics/reporting dashboards

## Author

Pranav Tambe — built as a comprehensive, real-world backend engineering project covering the full development lifecycle from local setup through production cloud deployment.
