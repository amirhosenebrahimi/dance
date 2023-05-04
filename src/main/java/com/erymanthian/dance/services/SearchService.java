package com.erymanthian.dance.services;

import com.erymanthian.dance.entities.Event;
import com.erymanthian.dance.entities.auth.Company;
import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.entities.auth.User;
import com.erymanthian.dance.enums.DanceStyle;
import com.erymanthian.dance.enums.PayGrade;
import com.erymanthian.dance.repositories.DancerRepository;
import com.erymanthian.dance.repositories.EventRepository;
import com.erymanthian.dance.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final DancerRepository danceRepository;

    public List<Event> searchEvent(Authentication authentication, String query, int page, int size, Boolean myEvents, PayGrade startPayGrade, PayGrade endPayGrade, DanceStyle style, boolean nearby) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            String state = user.getState();
            String city = user.getCity();
            PageRequest pageRequest = PageRequest.of(page, size);
            if (user instanceof Company && myEvents == true) {
                return eventRepository.findByCompanyId(user.getId(), pageRequest);
            }
            if ((startPayGrade == null || endPayGrade == null) && style == null) {
                if (query == null && nearby) {
                    return eventRepository.findByStateAndCity(state, city, pageRequest);
                } else if (query != null && nearby) {
                    return eventRepository.findByStateAndCityAndNameContainsIgnoreCase(state, city, query, pageRequest);
                } else if (query == null) {
                    throw new RuntimeException();
                } else {
                    return eventRepository.findByNameContainsIgnoreCase(query, pageRequest);
                }
            } else if (startPayGrade == null || endPayGrade == null) {
                if (query == null && nearby) {
                    return eventRepository.findByStylesContainsAndStateAndCity(style, state, city, pageRequest);
                } else if (query != null && nearby) {
                    return eventRepository.findByStylesContainsAndStateAndCityAndNameContainsIgnoreCase(style, state, city, query, pageRequest);
                } else if (query == null) {
                    return eventRepository.findByStylesContains(style, pageRequest);
                } else {
                    return eventRepository.findByStylesContainsAndNameContainsIgnoreCase(style, query, pageRequest);
                }
            } else if (style == null) {
                if (query == null && nearby) {
                    return eventRepository.findByPayGradeBetweenAndStateAndCity(startPayGrade, endPayGrade, state, city, pageRequest);
                } else if (query != null && nearby) {
                    return eventRepository.findByPayGradeBetweenAndStateAndCityAndNameContainsIgnoreCase(startPayGrade, endPayGrade, state, city, query, pageRequest);
                } else if (query == null) {
                    return eventRepository.findByPayGradeBetween(startPayGrade, endPayGrade, pageRequest);
                } else {
                    return eventRepository.findByPayGradeBetweenAndNameContainsIgnoreCase(startPayGrade, endPayGrade, query, pageRequest);
                }
            } else {
                if (query == null && nearby) {
                    return eventRepository.findByStylesContainsAndPayGradeBetweenAndStateAndCity(style, startPayGrade, endPayGrade, state, city, pageRequest);
                } else if (query != null && nearby) {
                    return eventRepository.findByStylesContainsAndPayGradeBetweenAndStateAndCityAndNameContainsIgnoreCase(style, startPayGrade, endPayGrade, state, city, query, pageRequest);
                } else if (query == null) {
                    return eventRepository.findByStylesContainsAndPayGradeBetween(style, startPayGrade, endPayGrade, pageRequest);
                } else {
                    return eventRepository.findByStylesContainsAndPayGradeBetweenAndNameContainsIgnoreCase(style, startPayGrade, endPayGrade, query, pageRequest);
                }
            }
        } else throw new RegistrationService.JWTAuthenticationNeededException();
    }

    public List<Dancer> searchTalent(Authentication authentication, String query, boolean nearby, int page, int size) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            String state = user.getState();
            String city = user.getCity();
            PageRequest pageRequest = PageRequest.of(page, size);
            if (nearby) {
                return danceRepository.findAllByStateAndCity(state, city, pageRequest);
            } else throw new FuckYouException();
        } else throw new RegistrationService.JWTAuthenticationNeededException();
    }

    @StandardException
    private static class FuckYouException extends RuntimeException {
    }
}
