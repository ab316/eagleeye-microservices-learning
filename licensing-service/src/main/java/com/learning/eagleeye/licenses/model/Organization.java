package com.learning.eagleeye.licenses.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash("organization")
@Data @With @NoArgsConstructor @AllArgsConstructor
public class Organization {
    @Id
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
