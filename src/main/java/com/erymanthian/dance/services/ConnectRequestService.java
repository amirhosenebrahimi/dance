package com.erymanthian.dance.services;

import com.erymanthian.dance.dtos.ConnectRequestCreateDTO;
import com.erymanthian.dance.entities.ConnectRequest;
import com.erymanthian.dance.repositories.ConnectRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConnectRequestService {

    private final ConnectRequestRepository connectRequestRepository;

    public ConnectRequest create(Authentication authentication, ConnectRequestCreateDTO connectRequestCreateDTO) throws Exception {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestRepository.findByTargetDancerAndSourceDancerAndStatusLessThan(connectRequestCreateDTO.getTargetUser(), userId, (short) 2);
            if (!connect.isEmpty()) {
                throw new ConnectionException();
            }
            return connectRequestRepository.save(new ConnectRequest(connectRequestCreateDTO.getTargetUser(), userId, (short) 0));
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public List<ConnectRequest> findMyRequest(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            return connectRequestRepository.findByTargetDancerOrSourceDancer(userId, userId).stream().map(connectRequest -> {
                if (connectRequest.getSourceDancer().equals(userId)) {
                    connectRequest.setIsForMe(false);
                    return connectRequest;
                }
                connectRequest.setIsForMe(true);
                return connectRequest;
            }).collect(Collectors.toList());
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public ConnectRequest updateStatus(Authentication authentication, Long id, Short status) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestRepository.findById(id);
            if (connect.isEmpty()) {
                throw new ConnectionException();
            }
            if (!connect.get().getTargetDancer().equals(userId) || connect.get().getStatus() != 0) {
                throw new ConnectionException();
            }
            connect.get().setStatus(status);
            return connectRequestRepository.save(connect.get());
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public Optional<ConnectRequest> findApprovedConnection(Long userId, Long id) {
        return connectRequestRepository.findById(id).filter(connectRequest -> connectRequest.getStatus() == 1)
                .filter(connectRequest -> connectRequest.getTargetDancer().equals(userId) ||
                        connectRequest.getSourceDancer().equals(userId));
    }

    @StandardException
    public static class ConnectionException extends RuntimeException{

    }
}
