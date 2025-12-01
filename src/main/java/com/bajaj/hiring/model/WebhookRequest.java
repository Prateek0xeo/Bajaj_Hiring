package com.bajaj.hiring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for webhook generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {
    private String name;
    private String regNo;
    private String email;
}
