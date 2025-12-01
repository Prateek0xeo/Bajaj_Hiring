# Setup and Deployment Guide

## Prerequisites Installation

### Option 1: Install Maven (Recommended)
1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH: `C:\Program Files\Apache\maven\bin`
4. Verify: `mvn -version`

### Option 2: Use Maven Wrapper (Included)
If you don't want to install Maven globally, you can use the Maven Wrapper:

#### Windows:
```bash
.\mvnw.cmd clean package
```

#### Linux/Mac:
```bash
./mvnw clean package
```

## Building the Application

### Step 1: Update Your Details
Edit `src/main/java/com/bajaj/hiring/service/HiringChallengeService.java` and update:
```java
private static final String CANDIDATE_NAME = "Your Full Name";
private static final String REG_NO = "Your Registration Number";
private static final String EMAIL = "your.email@example.com";
```

### Step 2: Build the JAR
```bash
mvn clean package
```
Or with wrapper:
```bash
.\mvnw.cmd clean package
```

This creates: `target/hiring-challenge-1.0.0.jar`

### Step 3: Run the Application
```bash
java -jar target/hiring-challenge-1.0.0.jar
```

Or directly with Maven:
```bash
mvn spring-boot:run
```

## What Happens When You Run

The application will:
1. ✅ Start Spring Boot
2. ✅ Send POST to generate webhook
3. ✅ Receive webhook URL and JWT token
4. ✅ Send SQL solution to webhook
5. ✅ Log all responses

Check console for detailed logs!

## SQL Query Explanation

### Problem Breakdown
- **Filter**: Employees with ANY payment > ₹70,000
- **Calculate**: Average age (as of 2025-12-01)
- **Concatenate**: Up to 10 employee names
- **Group by**: Department
- **Order by**: DEPARTMENT_ID DESC

### Query Structure
```sql
WITH QualifyingEmployees AS (
    -- Get distinct employees who have received payments > 70000
    SELECT DISTINCT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB, 
                    e.DEPARTMENT, d.DEPARTMENT_NAME
    FROM EMPLOYEE e
    INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
    INNER JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID
    WHERE p.AMOUNT > 70000
),
EmployeeAges AS (
    -- Calculate age and assign row numbers for limiting names
    SELECT 
        DEPARTMENT,
        DEPARTMENT_NAME,
        EMP_ID,
        CONCAT(FIRST_NAME, ' ', LAST_NAME) AS FULL_NAME,
        -- Accurate age calculation considering birth month/day
        TIMESTAMPDIFF(YEAR, DOB, '2025-12-01') - 
        CASE 
            WHEN DATE_FORMAT(DOB, '%m%d') > DATE_FORMAT('2025-12-01', '%m%d') 
            THEN 1 
            ELSE 0 
        END AS AGE,
        ROW_NUMBER() OVER (PARTITION BY DEPARTMENT ORDER BY EMP_ID) AS RN
    FROM QualifyingEmployees
)
SELECT 
    DEPARTMENT_NAME,
    ROUND(AVG(AGE), 2) AS AVERAGE_AGE,
    -- Concatenate only first 10 employees (RN <= 10)
    GROUP_CONCAT(
        CASE WHEN RN <= 10 THEN FULL_NAME END 
        ORDER BY EMP_ID 
        SEPARATOR ', '
    ) AS EMPLOYEE_LIST
FROM EmployeeAges
GROUP BY DEPARTMENT, DEPARTMENT_NAME
ORDER BY DEPARTMENT DESC
```

### Expected Results (from sample data)
Based on the sample data provided:

**Qualifying Employees** (payments > 70,000):
- EMP_ID 1: John Williams (Engineering, Age 45) - payment 70,837.00
- EMP_ID 2: Sarah Johnson (Finance, Age 35) - payment 72,984.00
- EMP_ID 3: Michael Smith (Engineering, Age 40) - payment 70,098.00
- EMP_ID 4: Emily Brown (Sales, Age 33) - payments 71,876.00 & 74,998.00
- EMP_ID 5: David Jones (Marketing, Age 37) - payment 71,475.00
- EMP_ID 6: Olivia Davis (HR, Age 30) - payment 70,198.00

**Output** (DEPARTMENT_ID DESC: 5→4→3→2→1):
| DEPARTMENT_NAME | AVERAGE_AGE | EMPLOYEE_LIST          |
|-----------------|-------------|------------------------|
| Marketing       | 37.00       | David Jones            |
| Sales           | 33.00       | Emily Brown            |
| Engineering     | 42.50       | John Williams, Michael Smith |
| Finance         | 35.00       | Sarah Johnson          |
| HR              | 30.00       | Olivia Davis           |

## Troubleshooting

### Issue: Maven not found
**Solution**: Install Maven or use Maven Wrapper (mvnw)

### Issue: Java version error
**Solution**: Ensure Java 17+ is installed
```bash
java -version
```

### Issue: Build fails
**Solution**: Check internet connection (Maven downloads dependencies)

### Issue: Application doesn't start
**Solution**: Check port 8080 is free or change in `application.properties`

### Issue: Webhook POST fails
**Solution**: 
- Check internet connection
- Verify the webhook URL is accessible
- Check logs for detailed error messages

## Alternative: Using RestTemplate
If WebClient causes issues, switch to RestTemplate:

1. Edit `HiringChallengeService.java`:
   - Comment out the `@Service` annotation

2. Edit `HiringChallengeServiceRestTemplate.java`:
   - Uncomment the `@Service` annotation

3. Rebuild and run

## Testing Locally (Without Real API)
To test the SQL logic without hitting real endpoints:
1. Comment out the `@EventListener` in HiringChallengeService
2. Create a test class to call `constructSqlQuery()` directly
3. Verify the SQL output

## Project Structure
```
c:/Coding/Bajaj/
├── src/
│   └── main/
│       ├── java/com/bajaj/hiring/
│       │   ├── BajajHiringApplication.java
│       │   ├── model/
│       │   │   ├── WebhookRequest.java
│       │   │   ├── WebhookResponse.java
│       │   │   └── SqlSubmissionRequest.java
│       │   └── service/
│       │       ├── HiringChallengeService.java (WebClient - Active)
│       │       └── HiringChallengeServiceRestTemplate.java (Backup)
│       └── resources/
│           └── application.properties
├── pom.xml
├── README.md
└── .gitignore
```

## Submission Checklist
- [ ] Updated candidate details (name, regNo, email)
- [ ] Verified regNo last 2 digits are even (for Question 2)
- [ ] Built successfully: `mvn clean package`
- [ ] JAR exists: `target/hiring-challenge-1.0.0.jar`
- [ ] Run locally and check logs
- [ ] Verified submission response is successful
- [ ] Ready to submit JAR file

## Contact & Support
If you encounter issues:
1. Check the logs in console output
2. Verify all prerequisites are installed
3. Ensure internet connectivity
4. Review the SQL query logic against sample data
