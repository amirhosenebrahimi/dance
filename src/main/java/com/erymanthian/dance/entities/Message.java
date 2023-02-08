package com.erymanthian.dance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String file;
    private Long connectionId;
    private Long time;

    private Long userId;

    public Message(String message, String file, Long connectionId, Long time, Long userId) {
        this.message = message;
        this.file = file;
        this.connectionId = connectionId;
        this.time = time;
        this.userId = userId;
    }
}
