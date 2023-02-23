package com.erymanthian.dance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

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
    private String companyName;

    @Transient
    private String dancerName;

    @Transient
    private String eventName;

    @Transient
    private String image;

    @Transient
    private String lastname;

    @Transient
    private String resource;

    public ConnectRequest(Long id, Integer eventId, Long sourceDancer, Short status,
                          String dancerName, String eventName, String lastname, String image) {
        this.id = id;
        this.eventId = eventId;
        this.sourceDancer = sourceDancer;
        this.status = status;
        this.dancerName = dancerName;
        this.eventName = eventName;
        this.lastname = lastname;
        this.image = image;
    }

    public ConnectRequest(Long id, Integer eventId, Long sourceDancer, Short status, String companyName, String eventName) {
        this.id = id;
        this.eventId = eventId;
        this.sourceDancer = sourceDancer;
        this.status = status;
        this.companyName = companyName;
        this.eventName = eventName;
    }

    public ConnectRequest(Integer eventId, Long sourceDancer, Short status) {
        this.eventId = eventId;
        this.sourceDancer = sourceDancer;
        this.status = status;
    }
}
