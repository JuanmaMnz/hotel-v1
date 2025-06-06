package com.hotel.Image.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageProperties {

    @Value("${MAX_FILE_SIZE}")
    private String maxFileSizeStr;

    @Value("${ALLOWED_TYPES}")
    private String allowedTypesStr;

    public long getMaxFileSizeInBytes() {
        if (maxFileSizeStr == null || maxFileSizeStr.isEmpty()) {
            return 5 * 1024 * 1024;
        }

        String sizeStr = maxFileSizeStr.trim().toUpperCase();

        try {
            if (sizeStr.endsWith("KB")) {
                return Long.parseLong(sizeStr.replace("KB", "").trim()) * 1024;
            } else if (sizeStr.endsWith("MB")) {
                return Long.parseLong(sizeStr.replace("MB", "").trim()) * 1024 * 1024;
            } else if (sizeStr.endsWith("GB")) {
                return Long.parseLong(sizeStr.replace("GB", "").trim()) * 1024 * 1024 * 1024;
            } else {
                return Long.parseLong(sizeStr);
            }
        } catch (NumberFormatException e) {
            return 5 * 1024 * 1024;
        }
    }

    public List<String> getAllowedTypes() {
        if (allowedTypesStr == null || allowedTypesStr.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(allowedTypesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(type -> "image/" + type.toLowerCase())
                .collect(Collectors.toList());
    }

}