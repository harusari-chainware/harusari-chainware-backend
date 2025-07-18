package com.harusari.chainware.common.properties;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@ConfigurationProperties(prefix = "fastapi")
public class FastApiProperties {

    private String url;

    public String getFullUrl(String path) {
        return url + path;
    }

}