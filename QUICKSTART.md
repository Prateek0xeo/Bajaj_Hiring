# Quick Start Guide

## ⚡ Fastest Way to Run

### Step 1: Update Your Details (REQUIRED)
Open: `src/main/java/com/bajaj/hiring/service/HiringChallengeService.java`

Find lines 25-27 and update:
```java
private static final String CANDIDATE_NAME = "Your Full Name Here";
private static final String REG_NO = "Your Registration Number";
private static final String EMAIL = "your.email@example.com";
```

### Step 2: Build (Choose One)

**Option A: If Maven is installed**
```cmd
mvn clean package
```

**Option B: Using Maven Wrapper (No installation needed)**
```cmd
mvnw.cmd clean package
```

### Step 3: Run
```cmd
java -jar target/hiring-challenge-1.0.0.jar
```

## 📋 What to Expect

The console will show:
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

## 🔍 SQL Query Preview

The embedded SQL query solves:
- **Filter**: Employees with ANY payment > ₹70,000
- **Calculate**: Average age (as of 2025-12-01)
- **Aggregate**: Concatenate up to 10 employee names per department
- **Sort**: By DEPARTMENT_ID DESC

**Expected Output Structure**:
| DEPARTMENT_NAME | AVERAGE_AGE | EMPLOYEE_LIST |
|-----------------|-------------|---------------|
| Marketing       | 37.00       | David Jones   |
| Sales           | 33.00       | Emily Brown   |
| Engineering     | 42.50       | John Williams, Michael Smith |
| Finance         | 35.00       | Sarah Johnson |
| HR              | 30.00       | Olivia Davis  |

## ❗ Common Issues

**"mvn is not recognized"** → Use `mvnw.cmd` instead

**"JAVA_HOME not found"** → Install Java 17+ and set JAVA_HOME

**Build fails** → Check internet connection (downloads dependencies)

**Port 8080 in use** → Change port in `application.properties` or stop conflicting app

## 📦 Deliverable

After successful build:
- **JAR Location**: `target/hiring-challenge-1.0.0.jar`
- **Size**: ~30-40 MB (includes all dependencies)
- **Runnable**: Yes, fully self-contained

## 🎯 Verification Checklist

- [x] Spring Boot 3.x application
- [x] Uses WebClient for HTTP requests
- [x] Executes on startup (ApplicationReadyEvent)
- [x] Generates webhook and parses response
- [x] Constructs SQL query for Question 2 (even regNo)
- [x] Submits to webhook with JWT authorization
- [x] Logs all steps (SLF4J)
- [x] No database integration needed
- [x] Executable JAR with spring-boot-maven-plugin
- [x] Exits gracefully after submission

## 📞 Need Help?

1. Check `SETUP_GUIDE.md` for detailed explanations
2. Review `README.md` for architecture details
3. Check console logs for error messages
4. Verify your details in HiringChallengeService.java

---

**Ready to submit?** Run the JAR, verify success logs, and submit!
