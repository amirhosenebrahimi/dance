package com.erymanthian.dance.services;

import com.erymanthian.dance.dtos.auth.*;
import com.erymanthian.dance.entities.auth.Code;
import com.erymanthian.dance.entities.auth.Company;
import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.entities.auth.User;
import com.erymanthian.dance.repositories.CodeRepository;
import com.erymanthian.dance.repositories.UserRepository;
import com.erymanthian.dance.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final TokenService tokenService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final RandomGenerator generator = RandomGenerator.getDefault();
    private final Path path;

    public UserAndToken login(Authentication authentication) {
        if (authentication.getPrincipal() instanceof SecurityUser user) {
            User entity = userRepository.findByEmail(user.getUsername()).orElseThrow();
            return new UserAndToken(entity, tokenService.generateLoginToken(user));
        } else throw new NotLoggedInWithBasicAuthException();
    }

    public String register(String email, String password) {
        User user = createUser(email, password);
        sendVerificationCode(email, user.getId());
        return tokenService.generateRegisterToken(user);
    }

    private Dancer createUser(String email, String password) {
        String encodedPassword = encoder.encode(password);
        Dancer entity = new Dancer(email, encodedPassword);
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.getStep() > 0) throw new UserAlreadyExistsException();
            else userRepository.delete(user);
        });
        entity = userRepository.save(entity);
        return entity;
    }

    private void sendVerificationCode(String email, Long id) {
        if (codeRepository.existsById(id)) codeRepository.deleteById(id);

        int verificationCode = generator.nextInt(1000, 9999);
        Code code = new Code(id, String.valueOf(verificationCode), LocalDateTime.now().plus(Duration.ofMinutes(10)), Code.Type.EMAIL);
        codeRepository.save(code);
        emailService.sendMessage(email, String.valueOf(verificationCode));
    }

    public void resend(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            sendVerificationCode(user.getEmail(), user.getId());
        }
    }

    public String verify(Authentication authentication, String code) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = verifyEmail(code, token);
            Short step = user.getStep();
            if (step == 0) {
                user.setStep(++step);
                userRepository.save(user);
                //TODO: most of save calls are redundant due to the @Transactional
            } else throw new WrongStepException();
            return tokenService.generateVerifyToken(user);
        } else throw new JWTAuthenticationNeededException();
    }

    private User verifyEmail(String code, JwtAuthenticationToken token) {
        Optional<Code> savedCode = codeRepository.findById(token.getToken().getClaim(TokenService.USER_ID));
        savedCode.filter(code1 -> code1.getExpireAt().isAfter(LocalDateTime.now())).map(Code::getVerification)
                .filter(s -> s.equals(code)).orElseThrow(WrongCodeException::new);
        return userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
    }

    public String field(Authentication authentication, FieldOrActivityInDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = user.getStep();
            if (step == 1) {
                if (dto.field() == FieldOrActivityInDto.Field.COMPANY) {
                    userRepository.delete(user);
                    user = new Company(user.getId(), user.getEmail(), user.getPassword());
                }
                user.setStep(++step);
                user = userRepository.save(user);
                return tokenService.generateVerifyToken(user);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void createDancer(Authentication authentication, ProfileDancerInDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Dancer dancer = (Dancer) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = dancer.getStep();
            if (step == 2) {
                dancer.setStep(++step);
                dancer.setFirstName(dto.firstName());
                dancer.setLastName(dto.lastName());
                dancer.setSex(dto.sex());
                dancer.setBirthday(LocalDate.ofInstant(Instant.ofEpochSecond(dto.birthday()), TimeZone.getDefault().toZoneId()));
                dancer.setState(dto.state());
                dancer.setCity(dto.city());
                userRepository.save(dancer);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void createCompany(Authentication authentication, ProfileCompanyIn dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Company company = (Company) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = company.getStep();
            if (step == 2) {
                company.setStep(++step);
                company.setBusinessName(dto.businessName());
                company.setType(dto.type());
                company.setState(dto.state());
                company.setCity(dto.city());
                company.setStreet(dto.street());
                company.setStreet2(dto.street2());
                company.setZip(dto.zip());
                userRepository.save(company);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void appearanceDancer(Authentication authentication, DancerAppearance dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Dancer dancer = (Dancer) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = dancer.getStep();
            if (step == 3) {
                dancer.setStep(++step);
                dancer.setHairColor(dto.hairColor());
                dancer.setEyeColor(dto.eyeColor());
                dancer.setHeight(dto.height());
                userRepository.save(dancer);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void talentsDancer(Authentication authentication, Talents dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Dancer dancer = (Dancer) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = dancer.getStep();
            if (step == 4) {
                dancer.setStep(++step);
                dancer.setStyles(new HashSet<>(dto.danceStyles()));
                dancer.setOpportunityType(dto.opportunityType());
                dancer.setRepresented(dto.represented());
                dancer.setAffiliation(dto.affiliation());
                dancer.setExpertise(dto.expertise());
                userRepository.save(dancer);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void bio(Authentication authentication, BioDto bio) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            if (user instanceof Dancer dancer && dancer.getStep() == 5 ||
                    user instanceof Company company && company.getStep() == 3) {
                user.setStep((short) (user.getStep() + 1));
                user.setBio(bio.bio());
                userRepository.save(user);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }


    public void socialMedia(Authentication authentication, SocialMedia dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Dancer dancer = (Dancer) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = dancer.getStep();
            if (step == 6) {
                dancer.setStep(++step);
                dancer.setInstagram(dto.instagram());
                dancer.setTiktok(dto.tiktok());
                userRepository.save(dancer);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public String setImage(Authentication authentication, MultipartFile file) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            if (user instanceof Dancer dancer && dancer.getStep() == 6 || user instanceof Company company && company.getStep() == 4) {
                String id = UUID.randomUUID().toString();
                try {
                    file.transferTo(path.resolve(id));
                    user.setImage(id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                userRepository.save(user);
                return id;
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void deleteImage(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            String id = user.getImage();
            try {
                Files.delete(path.resolve(id));
                user.setImage(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(user);
        } else throw new JWTAuthenticationNeededException();
    }

    public String setBannerCompany(Authentication authentication, MultipartFile file) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Company company = (Company) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            Short step = company.getStep();
            if (step == 4) {
                String id = UUID.randomUUID().toString();
                try {
                    file.transferTo(path.resolve(id));
                    company.setImage(id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                userRepository.save(company);
                return id;
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();
    }

    public void deleteBannerCompany(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Company company = (Company) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            String id = company.getImage();
            try {
                Files.delete(path.resolve(id));
                company.setImage(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(company);
        } else throw new JWTAuthenticationNeededException();
    }


    public String addGalleryDancer(Authentication authentication, MultipartFile file) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Dancer dancer = (Dancer) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            String id = UUID.randomUUID().toString();
            try {
                file.transferTo(path.resolve(id));
                dancer.getGallery().add(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(dancer);
            return id;
        } else throw new JWTAuthenticationNeededException();
    }

    public void deleteGalleryDancer(Authentication authentication, IdDto file) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Dancer dancer = (Dancer) userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            try {
                dancer.getGallery().remove(file.id());
                Files.delete(path.resolve(file.id()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(dancer);
        } else throw new JWTAuthenticationNeededException();
    }

    public void accept(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            if (user instanceof Dancer dancer && dancer.getStep() == 6 || user instanceof Company company && company.getStep() == 4) {
                user.setStep((short) (user.getStep() + 1));
                userRepository.save(user);
            } else throw new WrongStepException();
        } else throw new JWTAuthenticationNeededException();

    }

    public void changeEmail(Authentication authentication, EmailDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            sendVerificationCode(dto.newEmail(), token.getToken().getClaim(TokenService.USER_ID));
        } else throw new JWTAuthenticationNeededException();
    }

    public void changeResendEmail(Authentication authentication, EmailDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            sendVerificationCode(dto.newEmail(), token.getToken().getClaim(TokenService.USER_ID));
        } else throw new JWTAuthenticationNeededException();
    }

    public String changeVerifyEmail(Authentication authentication, VerifyEmailDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = verifyEmail(dto.code(), token);
            user.setEmail(dto.newEmail());
            return tokenService.generateVerifyToken(user);
        } else throw new JWTAuthenticationNeededException();
    }

    public void changePassword(Authentication authentication, PasswordDto dto) {
        if (authentication instanceof JwtAuthenticationToken token) {
            User user = userRepository.findById(token.getToken().getClaim(TokenService.USER_ID)).orElseThrow();
            String encoded = encoder.encode(dto.password());
            user.setPassword(encoded);
        } else throw new JWTAuthenticationNeededException();
    }

    @StandardException
    public static class UserAlreadyExistsException extends RuntimeException {
    }

    @StandardException
    public static class WrongStepException extends RuntimeException {
    }

    @StandardException
    public static class NotLoggedInWithBasicAuthException extends RuntimeException {
    }

    @StandardException
    public static class JWTAuthenticationNeededException extends RuntimeException {
    }

    @StandardException
    public static class WrongCodeException extends RuntimeException {
    }


}