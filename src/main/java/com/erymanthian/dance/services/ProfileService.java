package com.erymanthian.dance.services;

import com.erymanthian.dance.entities.auth.Company;
import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.entities.auth.User;
import com.erymanthian.dance.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    public Dancer dancer(Authentication authentication, Long id) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user;
            if (id == null)
                user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow(UserNotFoundException::new);
            else user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
            if (user instanceof Dancer dancer)
                return dancer;
            else throw new WrongTypeException();
        } throw new RegistrationService.JWTAuthenticationNeededException();
    }

    public Company company(Authentication authentication, Long id) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user;
            if (id == null)
                user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow(UserNotFoundException::new);
            else user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
            if (user instanceof Company company)
                return company;
            else throw new WrongTypeException();
        } throw new RegistrationService.JWTAuthenticationNeededException();
    }

    @StandardException
    public static class UserNotFoundException extends RuntimeException {
    }

    @StandardException
    public static class WrongTypeException extends RuntimeException {
    }
}
