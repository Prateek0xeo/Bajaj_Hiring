package com.bajaj.hiring.service;

import com.bajaj.hiring.model.SqlSubmissionRequest;
import com.bajaj.hiring.model.WebhookRequest;
import com.bajaj.hiring.model.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service to execute the hiring challenge flow on application startup
 */
@Service
@Slf4j
public class HiringChallengeService {

    private static final String WEBHOOK_GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private static final String FALLBACK_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    // Update these with your actual details
    private static final String CANDIDATE_NAME = "Prateek Kumar";
    private static final String REG_NO = "22BCE2300"; // Even last two digits (47 is odd, but example given)
    private static final String EMAIL = "Prateekkumar2004@gmail.com";

    private final WebClient webClient;

    public HiringChallengeService() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Executes the complete flow when application is ready
     */
    @EventListener(ApplicationReadyEvent.class)
    public void executeChallenge() {
        log.info("=== Starting Bajaj Hiring Challenge Flow ===");

        try {
            // Step 1: Generate webhook
            log.info("Step 1: Generating webhook...");
            WebhookResponse webhookResponse = generateWebhook();

            if (webhookResponse == null || webhookResponse.getWebhook() == null) {
                log.error("Failed to generate webhook. Using fallback URL.");
                webhookResponse = new WebhookResponse(FALLBACK_WEBHOOK_URL, "");
            }

            log.info("Webhook URL: {}", webhookResponse.getWebhook());
            log.info("Access Token: {}",
                    webhookResponse.getAccessToken() != null ? "Received" : "Not provided");

            // Step 2: Construct SQL query
            log.info("Step 2: Constructing SQL query...");
            String sqlQuery = constructSqlQuery();
            log.info("SQL Query constructed successfully");
            log.debug("SQL Query: {}", sqlQuery);

            // Step 3: Submit the solution
            log.info("Step 3: Submitting solution...");
            submitSolution(webhookResponse.getWebhook(),
                    webhookResponse.getAccessToken(),
                    sqlQuery);

            log.info("=== Challenge Flow Completed Successfully ===");

        } catch (Exception e) {
            log.error("Error during challenge execution: {}", e.getMessage(), e);
        }
    }

    /**
     * Step 1: Generate webhook by sending POST request
     */
    private WebhookResponse generateWebhook() {
        WebhookRequest request = new WebhookRequest(CANDIDATE_NAME, REG_NO, EMAIL);

        try {
            WebhookResponse response = webClient.post()
                    .uri(WEBHOOK_GENERATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(WebhookResponse.class)
                    .onErrorResume(error -> {
                        log.error("Error generating webhook: {}", error.getMessage());
                        return Mono.empty();
                    })
                    .block();

            return response;

        } catch (Exception e) {
            log.error("Exception in generateWebhook: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Step 2: Construct the SQL query for Question 2 (Even regNo)
     * 
     * Requirements:
     * - Calculate average age of employees with ANY payment > 70,000
     * - Concatenate their full names (up to 10)
     * - Group by department
     * - Only departments with qualifying employees
     * - Order by DEPARTMENT_ID DESC
     */
    private String constructSqlQuery() {
        // SQL Query solving the problem
        // Using standard SQL with MySQL/PostgreSQL compatibility
        return "WITH QualifyingEmployees AS (" +
                "    SELECT DISTINCT " +
                "        e.EMP_ID, " +
                "        e.FIRST_NAME, " +
                "        e.LAST_NAME, " +
                "        e.DOB, " +
                "        e.DEPARTMENT, " +
                "        d.DEPARTMENT_NAME " +
                "    FROM EMPLOYEE e " +
                "    INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "    INNER JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
                "    WHERE p.AMOUNT > 70000 " +
                "), " +
                "EmployeeAges AS (" +
                "    SELECT " +
                "        DEPARTMENT, " +
                "        DEPARTMENT_NAME, " +
                "        EMP_ID, " +
                "        CONCAT(FIRST_NAME, ' ', LAST_NAME) AS FULL_NAME, " +
                "        TIMESTAMPDIFF(YEAR, DOB, '2025-12-01') - " +
                "        CASE " +
                "            WHEN DATE_FORMAT(DOB, '%m%d') > DATE_FORMAT('2025-12-01', '%m%d') " +
                "            THEN 1 " +
                "            ELSE 0 " +
                "        END AS AGE, " +
                "        ROW_NUMBER() OVER (PARTITION BY DEPARTMENT ORDER BY EMP_ID) AS RN " +
                "    FROM QualifyingEmployees " +
                ") " +
                "SELECT " +
                "    DEPARTMENT_NAME, " +
                "    ROUND(AVG(AGE), 2) AS AVERAGE_AGE, " +
                "    GROUP_CONCAT(" +
                "        CASE WHEN RN <= 10 THEN FULL_NAME END " +
                "        ORDER BY EMP_ID " +
                "        SEPARATOR ', ' " +
                "    ) AS EMPLOYEE_LIST " +
                "FROM EmployeeAges " +
                "GROUP BY DEPARTMENT, DEPARTMENT_NAME " +
                "ORDER BY DEPARTMENT DESC";
    }

    /**
     * Step 3: Submit the SQL solution to the webhook
     */
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SqlSubmissionRequest request = new SqlSubmissionRequest(sqlQuery);

        try {
            // Build the request with authorization header
            WebClient.RequestHeadersSpec<?> requestSpec = webClient.post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request);

            // Add authorization header if token is provided
            if (accessToken != null && !accessToken.isEmpty()) {
                // Try Bearer format first (standard JWT)
                if (!accessToken.startsWith("Bearer ")) {
                    requestSpec = requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                } else {
                    requestSpec = requestSpec.header(HttpHeaders.AUTHORIZATION, accessToken);
                }
            }

            String response = requestSpec
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(error -> {
                        log.error("Error submitting solution: {}", error.getMessage());
                        return Mono.just("Error: " + error.getMessage());
                    })
                    .block();

            log.info("Submission response: {}", response);

        } catch (Exception e) {
            log.error("Exception in submitSolution: {}", e.getMessage(), e);
        }
    }
}
