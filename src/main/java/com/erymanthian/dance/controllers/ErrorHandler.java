package com.erymanthian.dance.controllers;

import com.erymanthian.dance.services.ConnectRequestService;
import com.erymanthian.dance.services.MessageService;
import com.erymanthian.dance.services.ProfileService;
import com.erymanthian.dance.services.RegistrationService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Log
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            RegistrationService.UserAlreadyExistsException.class,
            RegistrationService.JWTAuthenticationNeededException.class,
            RegistrationService.WrongCodeException.class,
            RegistrationService.WrongStepException.class,
            RegistrationService.NotLoggedInWithBasicAuthException.class,
            RegistrationService.PhoneNumberParsingException.class
    })
    public ProblemDetail registrationErrorHandler(Exception e) {
        log.info(Arrays.toString(e.getStackTrace()));
        log.warning(e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({
            ProfileService.UserNotFoundException.class,
            ProfileService.WrongTypeException.class,
            ConnectRequestService.ConnectionException.class,
            MessageService.ConnectionException.class})
    public ProblemDetail profileErrorHandler(Exception e) {
        log.info(Arrays.toString(e.getStackTrace()));
        log.warning(e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getClass().getSimpleName());
    }
}
