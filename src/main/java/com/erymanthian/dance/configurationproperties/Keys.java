package com.erymanthian.dance.configurationproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "keys")
public record Keys(RSAPublicKey pub, RSAPrivateKey pri) {
}
