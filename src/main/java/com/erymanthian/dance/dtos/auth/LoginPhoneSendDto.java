package com.erymanthian.dance.dtos.auth;

import jakarta.validation.constraints.NotNull;

public record LoginPhoneSendDto(@NotNull String phoneNumber, String countryCode) {
}
