# Bajaj Hiring Challenge

Spring Boot application for the Bajaj Hiring Challenge - Question 2 (Even RegNo).

## 🎯 What This Does

This application automatically:
1. Generates a webhook on startup
2. Constructs an SQL query solving the hiring challenge problem
3. Submits the solution to the webhook with JWT authentication

## 🏗️ Built With

- Spring Boot 2.7.18
- WebClient (reactive HTTP client)
- Maven
- Java 8

## 👤 Candidate Details

- **Name**: Prateek Kumar
- **Registration No**: 22BCE2300
- **Email**: Prateekkumar2004@gmail.com

## 📦 Building the JAR

### Option 1: Local Build (Requires JDK 8)
```powershell
.\mvnw.cmd clean package
```

### Option 2: GitHub Actions Build (No JDK Required)
See **[GITHUB_BUILD_GUIDE.md](GITHUB_BUILD_GUIDE.md)** for instructions on building remotely.

## 🚀 Running the Application

```powershell
java -jar target/hiring-challenge-1.0.0.jar
```

## 📚 Documentation

- **[QUICKSTART.md](QUICKSTART.md)** - Quick start guide
- **[GITHUB_BUILD_GUIDE.md](GITHUB_BUILD_GUIDE.md)** - Build using GitHub Actions
- **[GIT_COMMANDS.md](GIT_COMMANDS.md)** - Git command reference
- **[SETUP_GUIDE.md](SETUP_GUIDE.md)** - Detailed setup instructions
- **[SQL_EXPLANATION.md](SQL_EXPLANATION.md)** - SQL query breakdown
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Complete project overview
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System architecture diagrams

## ✅ Build Status

[![Build JAR](../../actions/workflows/build.yml/badge.svg)](../../actions/workflows/build.yml)

## 📝 License

This is a private project for the Bajaj Hiring Challenge.
