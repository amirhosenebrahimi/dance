package com.erymanthian.dance.controllers;

import com.erymanthian.dance.dtos.DescriptionDto;
import com.erymanthian.dance.dtos.EventNameDto;
import com.erymanthian.dance.dtos.OpportunityTypeDto;
import com.erymanthian.dance.dtos.StylesAndPriceDto;
import com.erymanthian.dance.dtos.auth.IdDto;
import com.erymanthian.dance.dtos.auth.IsAppliedDto;
import com.erymanthian.dance.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    final EventService service;

    @Secured({"ROLE_COMPANY"})
    @PostMapping("/create")
    IdDto create(Authentication authentication, @RequestBody EventNameDto dto) {
        return new IdDto(service.create(authentication, dto).toString());
    }

    @Secured({"ROLE_COMPANY"})
    @PostMapping("/description")
    void addDescription(Authentication authentication, @RequestParam int id, @RequestBody DescriptionDto dto) {
        service.addDescription(authentication, id, dto);
    }

    @Secured({"ROLE_COMPANY"})
    @PostMapping("/stylesAndPrice")
    void addStylesAndPrice(Authentication authentication, @RequestParam int id, @RequestBody StylesAndPriceDto dto) {
        service.addStylesAndPrice(authentication, id, dto);
    }

    @Secured({"ROLE_COMPANY"})
    @PostMapping("/opportunities")
    void addOpportunities(Authentication authentication, @RequestParam int id, @RequestBody OpportunityTypeDto dto) {
        service.addOpportunities(authentication, id, dto);
    }

    @Secured({"ROLE_COMPANY"})
    @PostMapping("/image")
    public IdDto addImage(Authentication authentication, @RequestParam int id, @RequestPart MultipartFile file) {
        return new IdDto(service.addImage(authentication, id, file));
    }

    @Secured({"ROLE_COMPANY"})
    @DeleteMapping("/image")
    public void deleteImage(Authentication authentication, @RequestParam int id, @RequestBody IdDto dto) {
        service.deleteImage(authentication, id, dto);
    }

    @PostMapping("/apply")
    public void apply(Authentication authentication, @RequestParam int id) {
        service.apply(authentication, id);
    }

    @GetMapping("/apply")
    public IsAppliedDto isApplied(Authentication authentication, @RequestParam int id) {
        return new IsAppliedDto(service.isApplied(authentication, id));
    }

}