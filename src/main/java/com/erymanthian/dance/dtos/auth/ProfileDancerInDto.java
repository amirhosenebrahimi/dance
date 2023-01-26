package com.erymanthian.dance.dtos.auth;

public record ProfileDancerInDto(String firstName, String lastName, Sex sex, Long birthday, String state, String city) {
}
