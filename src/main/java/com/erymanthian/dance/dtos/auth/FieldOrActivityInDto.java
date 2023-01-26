package com.erymanthian.dance.dtos.auth;

public record FieldOrActivityInDto(Field field) {
    public enum Field {DANCER, COMPANY}
}
