package com.erymanthian.dance.services;

import com.erymanthian.dance.dtos.DescriptionDto;
import com.erymanthian.dance.dtos.EventNameDto;
import com.erymanthian.dance.dtos.OpportunityTypeDto;
import com.erymanthian.dance.dtos.StylesAndPriceDto;
import com.erymanthian.dance.dtos.auth.IdDto;
import com.erymanthian.dance.entities.Event;
import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.repositories.EventRepository;
import com.erymanthian.dance.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    final EventRepository repository;
    final UserRepository userRepository;
    final Path path;

    public Integer create(Authentication authentication, EventNameDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = new Event(userId, dto.name(), dto.state(), dto.city());
            event = repository.save(event);
            return event.getId();
        }
        throw new InvalidAuthenticationMethod();
    }


    public Optional<Event> findById(Integer id, Long companyId) {
        return repository.findById(id).filter(event -> event.getCompanyId().equals(companyId));
    }

    public void addDescription(Authentication authentication, int id, DescriptionDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            if (!Objects.equals(event.getCompanyId(), userId)) throw new AccessDenied();
            event.setDescription(dto.description());
            event.setStep(1);
            repository.save(event);
        } else throw new InvalidAuthenticationMethod();
    }

    public void addStylesAndPrice(Authentication authentication, int id, StylesAndPriceDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            if (!Objects.equals(event.getCompanyId(), userId)) throw new AccessDenied();
            event.setStyles(new HashSet<>(dto.styles()));
            event.setPayGrade(dto.payGrade());
            event.setStep(2);
            repository.save(event);
        } else throw new InvalidAuthenticationMethod();
    }

    public void addOpportunities(Authentication authentication, int id, OpportunityTypeDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            if (!Objects.equals(event.getCompanyId(), userId)) throw new AccessDenied();
            event.setOpportunities(new HashSet<>(dto.opportunities()));
            event.setStep(3);
            repository.save(event);
        } else throw new InvalidAuthenticationMethod();
    }

    public String addImage(Authentication authentication, int id, MultipartFile file) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            if (!Objects.equals(event.getCompanyId(), userId)) throw new AccessDenied();
            String imageId = UUID.randomUUID().toString();
            try {
                file.transferTo(path.resolve(imageId));
                event.getGallery().add(imageId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            repository.save(event);
            return imageId;
        }
        throw new InvalidAuthenticationMethod();
    }

    public void deleteImage(Authentication authentication, int id, IdDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            if (!Objects.equals(event.getCompanyId(), userId)) throw new AccessDenied();
            try {
                event.getGallery().remove(dto.id());
                Files.delete(path.resolve(dto.id()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            repository.save(event);
        } else throw new InvalidAuthenticationMethod();
    }

  /*  public void apply(Authentication authentication, int id) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            Dancer e = (Dancer) userRepository.findById(userId).orElseThrow();
            event.getDancers().add(e);
            repository.save(event);
        } else throw new InvalidAuthenticationMethod();
    }

    public Boolean isApplied(Authentication authentication, int id) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Long userId = (Long) token.getTokenAttributes().get(TokenService.USER_ID);
            Event event = repository.findById(id).orElseThrow();
            return event.getDancers().stream().map(Dancer::getId).anyMatch(userId::equals);
        } else throw new InvalidAuthenticationMethod();
    }*/

    @StandardException
    static class InvalidAuthenticationMethod extends RuntimeException {
    }

    @StandardException
    static class AccessDenied extends RuntimeException {
    }
}
