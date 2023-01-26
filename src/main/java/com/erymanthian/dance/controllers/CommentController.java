package com.erymanthian.dance.controllers;

import com.erymanthian.dance.dtos.CommentDto;
import com.erymanthian.dance.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    final CommentService service;

    @PostMapping
    void addComment(Authentication authentication, @RequestParam long companyId, @RequestBody CommentDto commentDto) {
        service.addComment(authentication, companyId, commentDto);
    }

}
