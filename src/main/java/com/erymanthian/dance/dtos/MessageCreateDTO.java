package com.erymanthian.dance.dtos;

import lombok.Data;

@Data
public class MessageCreateDTO {

    private Long connectId;

    private String message;
}
