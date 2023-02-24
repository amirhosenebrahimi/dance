package com.erymanthian.dance.entities.auth;

import com.erymanthian.dance.dtos.auth.FieldOrActivityInDto;
import com.erymanthian.dance.entities.Comment;
import com.erymanthian.dance.enums.CompanyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DiscriminatorValue("COMPANY")
public class Company extends User {
    private String businessName;
    @Enumerated
    private CompanyType type;

    private String banner;
    private String street;
    private String street2;
    private Integer zip;


    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();


    public Company(Long id, String email, String password) {
        super(email, password);
        this.setId(id);
        this.setRole("COMPANY");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Company company = (Company) o;
        return getId() != null && Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
