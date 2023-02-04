package com.erymanthian.dance.controllers;

import com.erymanthian.dance.dtos.MessageCreateDTO;
import com.erymanthian.dance.entities.Message;
import com.erymanthian.dance.services.MessageService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> create(Authentication authentication, @RequestBody MessageCreateDTO message) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(authentication, message));
    }

    @PostMapping("/{connectionId}")
    public ResponseEntity<Message> createImage(Authentication authentication, @PathVariable Long connectionId, @RequestPart MultipartFile multipartFile) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createImage(authentication, connectionId, multipartFile));
    }

    @GetMapping("/{connectionId}")
    public ResponseEntity<List<Message>> getMessages(Authentication authentication, @PathVariable Long connectionId) {
        return ResponseEntity.ok(messageService.getMessages(authentication, connectionId));
    }

}
