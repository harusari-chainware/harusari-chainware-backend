package com.harusari.chainware.common.properties;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@ConfigurationProperties(prefix = "fastapi")
public class FastApiProperties {

    private String url;
    private String port;

    public String getFullUrl(String path) {
        return url + ":" + port + path;
    }

}