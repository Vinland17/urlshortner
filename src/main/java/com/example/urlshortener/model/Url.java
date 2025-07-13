package com.example.urlshortener.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "urls")
public class Url {

    @Id
    private String id;

    @Field("original_url")
    private String originalUrl;

    @Field("short_code")
    private String shortCode;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("click_count")
    private Long clickCount;

    public Url() {
        this.createdAt = LocalDateTime.now();
        this.clickCount = 0L;
    }

    public Url(String originalUrl, String shortCode) {
        this();
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public void incrementClickCount() {
        this.clickCount++;
    }
}