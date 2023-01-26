package com.erymanthian.dance.dtos;


import org.hibernate.validator.constraints.Range;

public record CommentDto(String text, @Range(min = 1, max = 5) int rate) {
}
