package com.erymanthian.dance.dtos.auth;


import jakarta.validation.constraints.Email;

public record RegisterInDto(@Email String email, String password) {
}
