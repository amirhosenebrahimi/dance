package com.erymanthian.dance.dtos;

import jakarta.validation.constraints.Email;

public record ForgotDto(@Email String email) {
}
