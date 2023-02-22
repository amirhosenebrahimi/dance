package com.erymanthian.dance.dtos.auth;

import jakarta.validation.constraints.NotNull;

public record LoginPhoneVerifyDto(@NotNull String phoneNumber, String countryCode, String code) {
}
