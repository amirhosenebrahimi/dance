package com.erymanthian.dance.dtos.auth;

import jakarta.validation.constraints.NotNull;

public record RegisterPhoneInDto(@NotNull String phoneNumber, String countryCode) {
}
