package com.erymanthian.dance.controllers;

import com.erymanthian.dance.entities.auth.Company;
import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final ProfileService service;

    @GetMapping("/dancer")
    public Dancer dancer(Authentication authentication, @RequestParam(required = false) Long id) {
        return service.dancer(authentication, id);
    }

    @GetMapping("/company")
    public Company company(Authentication authentication, @RequestParam(required = false) Long id) {
        return service.company(authentication, id);
    }
}
