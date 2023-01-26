package com.erymanthian.dance.entities.auth;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Inheritance
@DiscriminatorColumn(name = "role")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String email;
    private String password;
    @Column(name = "role", insertable = false, updatable = false)
    private String role;
    //TODO: use enum
    /**
     *      DANCER                  COMPANY
     *  0   EMAIL                   EMAIL
     *  1   VERIFIED                VERIFIED
     *  2   FIELD                   FIELD
     *  3   PROFILE                 PROFILE
     *  4   APPEARANCE              BIO
     *  5   TALENT                  IMAGE BANNER ACCEPT
     *  6   BIO                     PAYMENT
     *  7   IMAGE GALLERY ACCEPT
     *  8   PAYMENT
     */
    private Short step;

    private String bio;

    private String image;

    private String state;
    private String city;

    protected User(String email, String password) {
        this.email = email;
        this.password = password;
        step = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
