# Architecture & Flow Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                      │
│                    (bajaj-hiring-challenge)                     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Application Startup
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│              ApplicationReadyEvent Listener                     │
│         (HiringChallengeService.executeChallenge())            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Step 1:    │    │     Step 2:      │    │    Step 3:      │
│  Generate    │───▶│  Construct SQL   │───▶│  Submit to      │
│  Webhook     │    │     Query        │    │   Webhook       │
└──────────────┘    └──────────────────┘    └─────────────────┘
```

## Component Diagram

```
┌────────────────────────────────────────────────────────────────┐
│                   BajajHiringApplication                       │
│                    (@SpringBootApplication)                    │
└────────────────────────────────────────────────────────────────┘
                              │
                              │ Runs
                              ▼
┌────────────────────────────────────────────────────────────────┐
│              HiringChallengeService (@Service)                 │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  - WebClient (HTTP client)                              │  │
│  │  - CANDIDATE_NAME, REG_NO, EMAIL (constants)           │  │
│  │  - WEBHOOK_GENERATE_URL, FALLBACK_WEBHOOK_URL          │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│  Methods:                                                       │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  • executeChallenge()        - Main orchestrator       │  │
│  │  • generateWebhook()          - POST to gen webhook     │  │
│  │  • constructSqlQuery()        - Build SQL string        │  │
│  │  • submitSolution()           - POST SQL to webhook     │  │
│  └─────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
                              │
                              │ Uses
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                      Model Objects (DTOs)                      │
│                                                                 │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────┐ │
│  │ WebhookRequest   │  │ WebhookResponse  │  │ SqlSubmission│ │
│  │  - name          │  │  - webhook       │  │   Request    │ │
│  │  - regNo         │  │  - accessToken   │  │  - finalQuery│ │
│  │  - email         │  │                  │  │              │ │
│  └──────────────────┘  └──────────────────┘  └──────────────┘ │
└────────────────────────────────────────────────────────────────┘
```

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         STEP 1: Generate Webhook                │
└─────────────────────────────────────────────────────────────────┘

    Application                                    API Server
        │                                               │
        │  POST /hiring/generateWebhook/JAVA          │
        ├──────────────────────────────────────────────▶│
        │  {                                            │
        │    "name": "John Doe",                       │
        │    "regNo": "REG12347",                      │
        │    "email": "john@example.com"               │
        │  }                                            │
        │                                               │
        │◀──────────────────────────────────────────────┤
        │  {                                            │
        │    "webhook": "https://...testWebhook/xyz",   │
        │    "accessToken": "eyJhbGc..."               │
        │  }                                            │
        │                                               │

┌─────────────────────────────────────────────────────────────────┐
│                    STEP 2: Construct SQL Query                  │
└─────────────────────────────────────────────────────────────────┘

    Application
        │
        │  Build SQL Query String
        ├─────────────────────────────┐
        │                             │
        │  WITH QualifyingEmployees   │
        │  AS (...)                   │
        │  EmployeeAges AS (...)      │
        │  SELECT ...                 │
        │                             │
        └─────────────────────────────┘
        │
        │  SQL Query Ready


┌─────────────────────────────────────────────────────────────────┐
│                    STEP 3: Submit Solution                      │
└─────────────────────────────────────────────────────────────────┘

    Application                                    Webhook URL
        │                                               │
        │  POST <webhook_url>                          │
        ├──────────────────────────────────────────────▶│
        │  Headers:                                     │
        │    Authorization: Bearer <accessToken>        │
        │    Content-Type: application/json             │
        │                                               │
        │  Body:                                        │
        │  {                                            │
        │    "finalQuery": "WITH QualifyingEmployees..."│
        │  }                                            │
        │                                               │
        │◀──────────────────────────────────────────────┤
        │  Response (success/error)                     │
        │                                               │
        └──▶ Log response & complete                   │
```

## SQL Query Structure

```
┌─────────────────────────────────────────────────────────────────┐
│                      SQL Query Architecture                     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  CTE 1: QualifyingEmployees                                     │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  EMPLOYEE ──┬──┐                                          │  │
│  │  DEPARTMENT ┘  │                                          │  │
│  │  PAYMENTS ─────┴─▶ Filter: AMOUNT > 70000               │  │
│  │                                                            │  │
│  │  Output: Employee details with DEPARTMENT_NAME           │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  CTE 2: EmployeeAges                                            │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Calculate:                                               │  │
│  │  • FULL_NAME = CONCAT(FIRST_NAME, ' ', LAST_NAME)       │  │
│  │  • AGE = TIMESTAMPDIFF(YEAR, DOB, '2025-12-01')        │  │
│  │         - (birthday not yet occurred ? 1 : 0)            │  │
│  │  • RN = ROW_NUMBER() OVER (PARTITION BY DEPT)           │  │
│  │                                                            │  │
│  │  Output: Employee with AGE and RN                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  Final SELECT                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  GROUP BY: DEPARTMENT, DEPARTMENT_NAME                   │  │
│  │                                                            │  │
│  │  Aggregations:                                            │  │
│  │  • AVERAGE_AGE = ROUND(AVG(AGE), 2)                     │  │
│  │  • EMPLOYEE_LIST = GROUP_CONCAT(                         │  │
│  │      CASE WHEN RN <= 10 THEN FULL_NAME END,             │  │
│  │      SEPARATOR ', '                                       │  │
│  │    )                                                       │  │
│  │                                                            │  │
│  │  ORDER BY: DEPARTMENT DESC                               │  │
│  │                                                            │  │
│  │  Output: DEPARTMENT_NAME | AVERAGE_AGE | EMPLOYEE_LIST  │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Class Relationships

```
┌────────────────────────────────────────────────────────────────┐
│                  BajajHiringApplication                        │
│                                                                 │
│  + main(String[] args): void                                   │
└────────────────────────────────────────────────────────────────┘
                              │
                              │ Contains
                              ▼
┌────────────────────────────────────────────────────────────────┐
│              HiringChallengeService                            │
│                                                                 │
│  - webClient: WebClient                                         │
│  - CANDIDATE_NAME: String                                       │
│  - REG_NO: String                                              │
│  - EMAIL: String                                               │
│  - WEBHOOK_GENERATE_URL: String                                │
│  - FALLBACK_WEBHOOK_URL: String                                │
│                                                                 │
│  + executeChallenge(): void                                     │
│  - generateWebhook(): WebhookResponse                          │
│  - constructSqlQuery(): String                                 │
│  - submitSolution(url, token, query): void                     │
└────────────────────────────────────────────────────────────────┘
            │                   │                   │
            │ Uses              │ Uses              │ Uses
            ▼                   ▼                   ▼
┌──────────────────┐  ┌──────────────────┐  ┌─────────────────┐
│ WebhookRequest   │  │ WebhookResponse  │  │ SqlSubmission   │
│                  │  │                  │  │    Request      │
│ - name: String   │  │ - webhook: Str   │  │ - finalQuery:   │
│ - regNo: String  │  │ - accessToken:   │  │     String      │
│ - email: String  │  │     String       │  │                 │
└──────────────────┘  └──────────────────┘  └─────────────────┘
```

## Execution Timeline

```
Time    Event
│
├──┬─▶ Spring Boot starts
│  │
│  ├─▶ Context initialization
│  │
│  ├─▶ Bean creation (HiringChallengeService)
│  │
│  └─▶ ApplicationReadyEvent fired
│
├────▶ executeChallenge() triggered
│
├────▶ [Step 1] POST to generateWebhook
│      │
│      ├─▶ Send request with candidate details
│      │
│      ├─▶ Receive webhook URL & JWT token
│      │
│      └─▶ Log webhook URL and token status
│
├────▶ [Step 2] Construct SQL query
│      │
│      ├─▶ Build CTE 1: QualifyingEmployees
│      │
│      ├─▶ Build CTE 2: EmployeeAges
│      │
│      ├─▶ Build Final SELECT
│      │
│      └─▶ Return complete SQL string
│
├────▶ [Step 3] Submit solution
│      │
│      ├─▶ Create request body with SQL
│      │
│      ├─▶ Add Authorization header (Bearer token)
│      │
│      ├─▶ POST to webhook URL
│      │
│      ├─▶ Receive response
│      │
│      └─▶ Log response
│
└────▶ Flow completed
       │
       └─▶ Application continues running
           (can be terminated)
```

## Technology Stack

```
┌────────────────────────────────────────────────────────────────┐
│                      Technology Layers                         │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│  Application Layer                                              │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Spring Boot 3.2.0                                        │  │
│  │  - @SpringBootApplication                                 │  │
│  │  - @Service, @EventListener                              │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│  HTTP Client Layer                                              │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  WebClient (Spring WebFlux)                              │  │
│  │  - Reactive HTTP requests                                 │  │
│  │  - Non-blocking I/O                                       │  │
│  │                                                            │  │
│  │  RestTemplate (Backup)                                    │  │
│  │  - Traditional HTTP requests                              │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│  Data Layer                                                     │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Jackson (JSON processing)                                │  │
│  │  - Serialization/Deserialization                          │  │
│  │  - Object mapping                                         │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│  Utility Layer                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Lombok                                                   │  │
│  │  - @Data, @Slf4j annotations                             │  │
│  │  - Reduces boilerplate                                    │  │
│  │                                                            │  │
│  │  SLF4J + Logback                                          │  │
│  │  - Logging framework                                      │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│  Build & Packaging Layer                                       │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Maven 3.9.5                                              │  │
│  │  - Dependency management                                  │  │
│  │  - Build lifecycle                                        │  │
│  │  - spring-boot-maven-plugin (executable JAR)             │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
```

---

This architecture ensures:
✅ Clean separation of concerns
✅ Testable components
✅ Reactive, non-blocking HTTP
✅ Comprehensive error handling
✅ Production-ready deployment
