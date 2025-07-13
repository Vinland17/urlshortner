package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public String shortenUrl(String originalUrl) {
        // Normalize the URL
        String normalizedUrl = UrlUtils.normalizeUrl(originalUrl);

        // Validate URL
        if (!UrlUtils.isValidUrl(normalizedUrl)) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        // Check if URL already exists
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(normalizedUrl);
        if (existingUrl.isPresent()) {
            return baseUrl + "/" + existingUrl.get().getShortCode();
        }

        // Generate unique short code
        String shortCode;
        do {
            shortCode = UrlUtils.generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));

        // Save to database
        Url url = new Url(normalizedUrl, shortCode);
        urlRepository.save(url);

        return baseUrl + "/" + shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        if (url.isPresent()) {
            Url urlEntity = url.get();
            urlEntity.incrementClickCount();
            urlRepository.save(urlEntity);
            return urlEntity.getOriginalUrl();
        }
        return null;
    }

    public Url getUrlStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode).orElse(null);
    }
}
