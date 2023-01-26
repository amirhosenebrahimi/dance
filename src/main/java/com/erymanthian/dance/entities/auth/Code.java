package com.erymanthian.dance.entities.auth;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Code {

    //TODO: set a unidirectional foreign key relation between Code and User
    @Id
    private Long userId;

    private String verification;

    @Basic
    private LocalDateTime expireAt;

    @Enumerated
    private Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Code code = (Code) o;
        return userId != null && Objects.equals(userId, code.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum Type {EMAIL, SMS}
}
