package com.erymanthian.dance.services;

import com.erymanthian.dance.dtos.CommentDto;
import com.erymanthian.dance.entities.Comment;
import com.erymanthian.dance.entities.Event;
import com.erymanthian.dance.entities.auth.Company;
import com.erymanthian.dance.entities.auth.User;
import com.erymanthian.dance.repositories.EventRepository;
import com.erymanthian.dance.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository repository;

    public void addComment(Authentication authentication, long companyId, CommentDto commentDto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Company company = (Company) repository.findById(companyId).orElseThrow();
            Comment comment = new Comment(commentDto.rate(), commentDto.text(), System.currentTimeMillis(), userId);
            company.getComments().add(comment);
        } else throw new EventService.InvalidAuthenticationMethod();
    }
}
