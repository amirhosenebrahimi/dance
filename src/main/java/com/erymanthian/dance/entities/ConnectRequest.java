package com.erymanthian.dance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConnectRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer eventId;

    private Long sourceDancer;

    private Short status;

    @Transient
    private Boolean isForMe;

    public ConnectRequest(Integer eventId, Long sourceDancer, Short status) {
        this.eventId = eventId;
        this.sourceDancer = sourceDancer;
        this.status = status;
    }
}
