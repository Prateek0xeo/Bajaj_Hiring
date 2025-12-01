# 📖 Documentation Index

Welcome to the Bajaj Hiring Challenge Spring Boot Application!

This project is a complete, production-ready solution that automates the hiring challenge flow on application startup.

---

## 🚀 Quick Navigation

### For Quick Start
- **[QUICKSTART.md](QUICKSTART.md)** - Get up and running in 3 steps
  - Update your details
  - Build the JAR
  - Run the application

### For Complete Setup
- **[SETUP_GUIDE.md](SETUP_GUIDE.md)** - Detailed installation and configuration
  - Maven installation options
  - Build instructions
  - Troubleshooting guide
  - Expected output examples

### For Understanding the Project
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Complete project overview
  - Feature compliance checklist
  - Execution flow diagram
  - Design decisions
  - Pre-submission checklist

### For SQL Query Details
- **[SQL_EXPLANATION.md](SQL_EXPLANATION.md)** - SQL query breakdown
  - Sample data analysis
  - Step-by-step query construction
  - Expected results
  - Edge cases handled

### For Architecture
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System architecture diagrams
  - Component structure
  - Data flow diagrams
  - Class relationships
  - Technology stack

### For General Information
- **[README.md](README.md)** - Project README
  - Overview
  - Project structure
  - Dependencies
  - Notes

---

## 📁 Project Files

### Source Code
```
src/main/java/com/bajaj/hiring/
├── BajajHiringApplication.java              # Main application class
├── model/
│   ├── WebhookRequest.java                  # Request DTO for webhook generation
│   ├── WebhookResponse.java                 # Response DTO from webhook
│   └── SqlSubmissionRequest.java            # Request DTO for SQL submission
└── service/
    ├── HiringChallengeService.java          # Main service (WebClient) ⭐
    └── HiringChallengeServiceRestTemplate.java  # Backup (RestTemplate)
```

### Configuration
```
src/main/resources/
└── application.properties                    # Spring Boot configuration

Root Directory:
├── pom.xml                                   # Maven dependencies
├── mvnw / mvnw.cmd                          # Maven wrapper scripts
└── .mvn/wrapper/                            # Maven wrapper JAR
```

---

## 🎯 What Does This Application Do?

### On Startup:

1. **Generates a Webhook**
   - POST to `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
   - Sends your name, regNo, and email
   - Receives webhook URL and JWT access token

2. **Constructs SQL Query**
   - Solves Question 2 (Even regNo)
   - Calculates average age of employees with payments > ₹70,000
   - Concatenates their names (up to 10 per department)
   - Groups by department, orders by DEPARTMENT_ID DESC

3. **Submits Solution**
   - POST to the received webhook URL
   - Includes JWT in Authorization header
   - Body contains the SQL query
   - Logs the response

---

## ⚡ How to Use This Documentation

### If you're new to the project:
1. Start with **QUICKSTART.md** to get the app running
2. Read **PROJECT_SUMMARY.md** for an overview
3. Check **ARCHITECTURE.md** to understand the design

### If you need to debug:
1. Review **SETUP_GUIDE.md** for troubleshooting
2. Check **SQL_EXPLANATION.md** for query logic
3. Look at console logs for detailed error messages

### If you want to understand the code:
1. Read **ARCHITECTURE.md** for component structure
2. Review **PROJECT_SUMMARY.md** for design decisions
3. Check source code comments

---

## ✅ Before You Start

### Required:
- ✅ Java 17 or higher installed
- ✅ Internet connection (for Maven dependencies)

### Optional:
- Maven 3.6+ (can use included wrapper instead)
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Must Update:
Open `src/main/java/com/bajaj/hiring/service/HiringChallengeService.java` and update lines 27-29:

```java
private static final String CANDIDATE_NAME = "Your Full Name";
private static final String REG_NO = "Your Registration Number";
private static final String EMAIL = "your.email@example.com";
```

---

## 🏃 Quick Start Commands

### Build
```cmd
mvnw.cmd clean package
```

### Run
```cmd
java -jar target/hiring-challenge-1.0.0.jar
```

### Build + Run (Maven)
```cmd
mvnw.cmd spring-boot:run
```

---

## 📊 Expected Console Output

```
=== Starting Bajaj Hiring Challenge Flow ===
Step 1: Generating webhook...
Webhook URL: https://...
Access Token: Received
Step 2: Constructing SQL query...
SQL Query constructed successfully
Step 3: Submitting solution...
Submission response: {...}
=== Challenge Flow Completed Successfully ===
```

---

## 🎓 Key Technologies Used

- **Spring Boot 3.2.0** - Application framework
- **WebClient** - Reactive HTTP client
- **Jackson** - JSON processing
- **Lombok** - Reduce boilerplate
- **SLF4J** - Logging
- **Maven** - Build tool

---

## 📦 Deliverable

After building, you'll have:
- **File**: `target/hiring-challenge-1.0.0.jar`
- **Size**: ~30-40 MB
- **Type**: Executable JAR (fully self-contained)

---

## 🐛 Common Issues

| Issue | Quick Fix |
|-------|----------|
| `mvn not recognized` | Use `mvnw.cmd` instead |
| `JAVA_HOME not found` | Install Java 17+ |
| Build fails | Check internet connection |
| Port 8080 in use | Stop conflicting apps |

For detailed troubleshooting, see **SETUP_GUIDE.md**.

---

## 📞 Documentation Structure

```
📖 INDEX.md (You are here)
    │
    ├─▶ 📄 QUICKSTART.md
    │   └─▶ Fastest way to get started
    │
    ├─▶ 📄 SETUP_GUIDE.md
    │   └─▶ Complete setup instructions
    │
    ├─▶ 📄 PROJECT_SUMMARY.md
    │   └─▶ Feature overview & checklist
    │
    ├─▶ 📄 SQL_EXPLANATION.md
    │   └─▶ SQL query deep dive
    │
    ├─▶ 📄 ARCHITECTURE.md
    │   └─▶ System design & diagrams
    │
    └─▶ 📄 README.md
        └─▶ General project info
```

---

## 🎯 Success Criteria

Your application should:
- ✅ Build without errors
- ✅ Run and complete all 3 steps
- ✅ Log webhook URL and token
- ✅ Submit SQL query successfully
- ✅ Show "Challenge Flow Completed Successfully"

---

## 🚀 Ready to Submit?

1. ✅ Updated your details in `HiringChallengeService.java`
2. ✅ Built successfully: `mvnw.cmd clean package`
3. ✅ Tested locally: `java -jar target/hiring-challenge-1.0.0.jar`
4. ✅ Verified success logs
5. ✅ JAR file ready: `target/hiring-challenge-1.0.0.jar`

**You're all set! 🎉**

---

## 📝 Additional Notes

- No manual endpoints needed - runs automatically on startup
- No database required - SQL is a static string
- Fully self-contained - JAR includes all dependencies
- Comprehensive logging - every step is logged
- Error resilient - graceful handling with fallbacks

---

**Need help? Check the specific documentation file for your question!**

📚 **Happy Coding!** 🚀
