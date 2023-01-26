package com.erymanthian.dance.controllers;

import com.erymanthian.dance.dtos.auth.*;
import com.erymanthian.dance.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService service;

    @GetMapping("/login")
    public UserAndToken login(Authentication authentication) {
        return service.login(authentication);
    }

    @PostMapping("/register")
    public TokenDto register(@RequestBody @Valid RegisterInDto dto) {
        return new TokenDto(service.register(dto.email(), dto.password()));
    }

    @PostMapping("/verify")
    public TokenDto verify(Authentication authentication, @RequestBody VerifyInRequest dto) {
        return new TokenDto(service.verify(authentication, dto.code()));
    }

    @GetMapping("/resend")
    public void resend(Authentication authentication) {
        service.resend(authentication);
    }

    @PostMapping("/field")
    public TokenDto field(Authentication authentication, @RequestBody FieldOrActivityInDto dto) {
        return new TokenDto(service.field(authentication, dto));
    }

    @PostMapping("/create/dancer")
    public void createDancer(Authentication authentication, @RequestBody ProfileDancerInDto dto) {
        service.createDancer(authentication, dto);
    }

    @PostMapping("/create/company")
    public void createCompany(Authentication authentication, @RequestBody ProfileCompanyIn dto) {
        service.createCompany(authentication, dto);
    }

    @PostMapping("/appearance/dancer")
    public void appearanceDancer(Authentication authentication, @RequestBody DancerAppearance dto) {
        service.appearanceDancer(authentication, dto);
    }

    @PostMapping("/talents/dancer")
    public void othersDancer(Authentication authentication, @RequestBody Talents dto) {
        service.talentsDancer(authentication, dto);
    }

    @PostMapping("/bio")
    public void bio(Authentication authentication, @RequestBody BioDto bio) {
        service.bio(authentication, bio);
    }

    @PostMapping("/image")
    public IdDto setImage(Authentication authentication, @RequestPart() MultipartFile file) {
        return new IdDto(service.setImage(authentication, file));
    }

    @DeleteMapping("/image")
    public void deleteImage(Authentication authentication) {
        service.deleteImage(authentication);
    }

    @PostMapping("/gallery/dancer")
    public IdDto addGalleryDancer(Authentication authentication, @RequestPart MultipartFile file) {
        return new IdDto(service.addGalleryDancer(authentication, file));
    }

    @DeleteMapping("/gallery/dancer")
    public void deleteGalleryDancer(Authentication authentication, @RequestBody IdDto dto) {
        service.deleteGalleryDancer(authentication, dto);
    }

    @PostMapping("/banner/company")
    public IdDto setBannerCompany(Authentication authentication, @RequestPart MultipartFile file) {
        return new IdDto(service.setBannerCompany(authentication, file));
    }

    @DeleteMapping("/banner/company")
    public void deleteBannerCompany(Authentication authentication) {
        service.deleteBannerCompany(authentication);
    }

    @PostMapping("/accept")
    public void accept(Authentication authentication) {
        service.accept(authentication);
    }

    @PatchMapping("/email")
    public void changeEmail(Authentication authentication, @RequestBody EmailDto dto) {
        service.changeEmail(authentication, dto);
    }

    @PatchMapping("/email/resend")
    public void changeResendEmail(Authentication authentication, @RequestBody EmailDto dto) {
        service.changeResendEmail(authentication, dto);
    }

    @PatchMapping("/email/verify")
    public TokenDto changeVerifyEmail(Authentication authentication, @RequestBody VerifyEmailDto dto) {
        return new TokenDto(service.changeVerifyEmail(authentication, dto));
    }

    @PatchMapping("/password")
    public void changeEmail(Authentication authentication, @RequestBody PasswordDto dto) {
        service.changePassword(authentication, dto);
    }
}
