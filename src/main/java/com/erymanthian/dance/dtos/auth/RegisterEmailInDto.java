package com.erymanthian.dance.dtos.auth;


import jakarta.validation.constraints.Email;

public record RegisterEmailInDto(@Email String email, String password) {
}
