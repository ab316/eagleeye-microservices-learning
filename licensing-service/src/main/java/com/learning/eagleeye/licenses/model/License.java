package com.learning.eagleeye.licenses.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;

@Entity
@Data @With @NoArgsConstructor @AllArgsConstructor
public class License {
    @Id
    @Column(nullable = false)
    private String licenseId;

    @Column(nullable = false)
    private String organizationId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String licenseType;
}
