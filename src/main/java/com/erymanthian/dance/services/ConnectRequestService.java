package com.erymanthian.dance.services;

import com.erymanthian.dance.dtos.ConnectRequestCreateDTO;
import com.erymanthian.dance.entities.ConnectRequest;
import com.erymanthian.dance.repositories.ConnectRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConnectRequestService {

    private final ConnectRequestRepository connectRequestRepository;
    private final EventService eventService;
    private final ProfileService profileService;
    private final Path path;


    public ConnectRequest create(Authentication authentication, ConnectRequestCreateDTO connectRequestCreateDTO) throws Exception {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestRepository.findByEventIdAndSourceDancer(connectRequestCreateDTO.getEventId(), userId);
            if (!connect.isEmpty()) {
                throw new ConnectionException();
            }
            return connectRequestRepository.save(new ConnectRequest(connectRequestCreateDTO.getEventId(), userId, (short) 0));

        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public List<ConnectRequest> findMyRequest(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var user = profileService.find(userId);
            if (user.getRole().equals("DANCER")) {
                return connectRequestRepository.findBySourceDancer(userId);
            } else if (user.getRole().equals("COMPANY")) {
                System.out.println( connectRequestRepository.findByCompany(userId));
                return connectRequestRepository.findByCompany(userId).stream()
                        .peek(connectRequest -> {
                            if (connectRequest.getImage() != null) {
                                try {
                                    connectRequest.setResource(Base64.getEncoder().encodeToString((Files.readAllBytes(path.resolve(connectRequest.getImage())))));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .toList();
            }
            throw new ConnectionException();

        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public ConnectRequest updateStatus(Authentication authentication, Long id, Short status) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestRepository.findById(id);

            if (connect.isEmpty()) {
                throw new ConnectionException();
            }
            var event = eventService.findById(connect.get().getEventId(), userId);
            if (event.isEmpty()) {
                throw new ConnectionException();
            }
            if (connect.get().getStatus() != 0) {
                throw new ConnectionException();
            }

            connect.get().setStatus(status);
            return connectRequestRepository.save(connect.get());
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public Optional<ConnectRequest> findApprovedConnection(Long userId, Long id) {
        return connectRequestRepository.findById(id).filter(connectRequest -> connectRequest.getStatus() == 1)
                .filter(connectRequest -> connectRequest.getEventId().equals(userId) ||
                        connectRequest.getSourceDancer().equals(userId));
    }

    public Optional<ConnectRequest> findById(Long connectionId) {
        return connectRequestRepository.findById(connectionId);
    }

    @StandardException
    public static class ConnectionException extends RuntimeException {

    }
}
