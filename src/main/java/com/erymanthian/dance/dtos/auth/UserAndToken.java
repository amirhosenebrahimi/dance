package com.erymanthian.dance.dtos.auth;

import com.erymanthian.dance.entities.auth.User;

public record UserAndToken(User user, String token) {
}
