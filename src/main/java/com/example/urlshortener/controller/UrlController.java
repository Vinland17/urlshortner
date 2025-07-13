package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String home() {
        try {
            ClassPathResource resource = new ClassPathResource("static/index.html");
            byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "<!DOCTYPE html><html><head><title>URL Shortener</title></head><body><h1>URL Shortener Service</h1><p>Frontend not found. Please ensure index.html is in src/main/resources/static/</p></body></html>";
        }
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, Object>> shortenUrl(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String originalUrl = request.get("url");
            if (originalUrl == null || originalUrl.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "URL cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }

            String shortUrl = urlService.shortenUrl(originalUrl);
            response.put("success", true);
            response.put("originalUrl", originalUrl);
            response.put("shortUrl", shortUrl);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "An error occurred while shortening the URL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);
            if (originalUrl != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(originalUrl));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable String shortCode) {
        Map<String, Object> response = new HashMap<>();

        Url url = urlService.getUrlStats(shortCode);
        if (url != null) {
            response.put("success", true);
            response.put("originalUrl", url.getOriginalUrl());
            response.put("shortCode", url.getShortCode());
            response.put("clickCount", url.getClickCount());
            response.put("createdAt", url.getCreatedAt());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "URL not found");
            return ResponseEntity.notFound().build();
        }
    }
}