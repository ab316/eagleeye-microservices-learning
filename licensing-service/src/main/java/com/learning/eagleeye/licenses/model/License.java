package com.learning.eagleeye.licenses.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data @With @NoArgsConstructor @AllArgsConstructor
public class License {
    private String id;
    private String organizationId;
    private String productName;
    private String licenseType;
}
