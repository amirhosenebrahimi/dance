package com.erymanthian.dance.entities;

import com.erymanthian.dance.entities.auth.Dancer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    private Long targetDancer;

    private Long sourceDancer;

    private Short status;

    @Transient
    private Boolean isForMe;

    public ConnectRequest(Long targetDancer, Long sourceDancer, Short status) {
        this.targetDancer = targetDancer;
        this.sourceDancer = sourceDancer;
        this.status = status;
    }
}
