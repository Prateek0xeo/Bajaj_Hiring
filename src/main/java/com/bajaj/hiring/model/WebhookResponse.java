package com.bajaj.hiring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model from webhook generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookResponse {
    private String webhook;
    private String accessToken;
}
