package com.bajaj.hiring.service;

import com.bajaj.hiring.model.SqlSubmissionRequest;
import com.bajaj.hiring.model.WebhookRequest;
import com.bajaj.hiring.model.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Alternative implementation using RestTemplate instead of WebClient
 * Uncomment @Service and comment the WebClient version to use this
 */
// @Service
@Slf4j
public class HiringChallengeServiceRestTemplate {

    private static final String WEBHOOK_GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private static final String FALLBACK_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    // Update these with your actual details
    private static final String CANDIDATE_NAME = "John Doe";
    private static final String REG_NO = "REG12347";
    private static final String EMAIL = "john@example.com";

    private final RestTemplate restTemplate;

    public HiringChallengeServiceRestTemplate() {
        this.restTemplate = new RestTemplate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executeChallenge() {
        log.info("=== Starting Bajaj Hiring Challenge Flow (RestTemplate) ===");

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

    private WebhookResponse generateWebhook() {
        WebhookRequest request = new WebhookRequest(CANDIDATE_NAME, REG_NO, EMAIL);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                    WEBHOOK_GENERATE_URL,
                    HttpMethod.POST,
                    entity,
                    WebhookResponse.class);

            return response.getBody();

        } catch (Exception e) {
            log.error("Exception in generateWebhook: {}", e.getMessage(), e);
            return null;
        }
    }

    private String constructSqlQuery() {
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

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SqlSubmissionRequest request = new SqlSubmissionRequest(sqlQuery);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Add authorization header if token is provided
            if (accessToken != null && !accessToken.isEmpty()) {
                if (!accessToken.startsWith("Bearer ")) {
                    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                } else {
                    headers.set(HttpHeaders.AUTHORIZATION, accessToken);
                }
            }

            HttpEntity<SqlSubmissionRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class);

            log.info("Submission response status: {}", response.getStatusCode());
            log.info("Submission response body: {}", response.getBody());

        } catch (Exception e) {
            log.error("Exception in submitSolution: {}", e.getMessage(), e);
        }
    }
}
