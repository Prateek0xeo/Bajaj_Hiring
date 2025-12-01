package com.bajaj.hiring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for SQL query submission
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlSubmissionRequest {
    private String finalQuery;
}
