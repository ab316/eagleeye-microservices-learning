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
    private String id;

    @Column(nullable = false)
    private String organizationId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String licenseType;

    @Transient
    private String organizationName;

    @Transient
    private String contactName;

    @Transient
    private String contactEmail;

    @Transient
    private String contactPhone;
}
