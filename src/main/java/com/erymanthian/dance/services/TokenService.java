package com.erymanthian.dance.services;

import com.erymanthian.dance.entities.auth.User;
import com.erymanthian.dance.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    public static final String USER_ID = "userId";
    private final JwtEncoder encoder;

    public String generateLoginToken(SecurityUser user) {
            return generateToken(user, Duration.ofDays(365));
    }

    public String generateRegisterToken(User user) {
        return generateToken(new SecurityUser(user), Duration.ofHours(1));
    }

    public String generateVerifyToken(User user) {
        return generateToken(new SecurityUser(user), Duration.ofDays(365));
    }


    private String generateToken(SecurityUser user, Duration amountToAdd) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(amountToAdd))
                .subject(user.getUsername())
                .claim("scope", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
                .claim("enabled", user.isEnabled())
                .claim(USER_ID, user.getUserId())
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
