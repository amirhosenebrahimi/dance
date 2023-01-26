package com.erymanthian.dance.configurationproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("resources")
public record Resources(String path, Float quality) {
}
