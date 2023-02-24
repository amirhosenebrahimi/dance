package com.erymanthian.dance.services;

import com.erymanthian.dance.dtos.MessageCreateDTO;
import com.erymanthian.dance.entities.Message;
import com.erymanthian.dance.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConnectRequestService connectRequestService;
    private final EventService eventService;
    private final Path path;

    public Message create(Authentication authentication, MessageCreateDTO message) throws Exception {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestService.findApprovedConnection(userId, message.getConnectId());
            if (connect.isEmpty()) {
                connect = connectRequestService.findById(message.getConnectId());
                if (connect.isEmpty())
                    throw new ConnectionException();
                if(eventService.findById(connect.get().getEventId(), userId).isEmpty()){
                    throw new ConnectionException();
                }
            }
            return messageRepository.save(new Message(message.getMessage(), null, message.getConnectId(), System.currentTimeMillis(), userId));
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public Message createImage(Authentication authentication, Long connectionId, MultipartFile multipartFile) throws Exception {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestService.findApprovedConnection(userId, connectionId);
            if (connect.isEmpty()) {
                connect = connectRequestService.findById(connectionId);
                if (connect.isEmpty())
                    throw new ConnectionException();
                if(eventService.findById(connect.get().getEventId(), userId).isEmpty()){
                    throw new ConnectionException();
                }
            }
            String uuid = UUID.randomUUID().toString();
            multipartFile.transferTo(path.resolve(uuid));
            return messageRepository.save(new Message(null, uuid, connectionId, System.currentTimeMillis(), userId));
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    public List<Message> getMessages(Authentication authentication, Long connectionId) {

        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            var connect = connectRequestService.findApprovedConnection(userId, connectionId);
            if (connect.isEmpty()) {
                connect = connectRequestService.findById(connectionId);
                if (connect.isEmpty())
                    throw new ConnectionException();
                if(eventService.findById(connect.get().getEventId(), userId).isEmpty()){
                    throw new ConnectionException();
                }
            }


            return messageRepository.findByConnectionId(connectionId);
        } else throw new EventService.InvalidAuthenticationMethod();
    }

    @StandardException
    public static class ConnectionException extends RuntimeException {
    }
}
