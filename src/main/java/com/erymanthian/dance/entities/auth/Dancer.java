package com.erymanthian.dance.entities.auth;

import com.erymanthian.dance.dtos.auth.Sex;
import com.erymanthian.dance.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DiscriminatorValue("DANCER")
public class Dancer extends User {

    private String firstName;
    private String lastName;
    @Enumerated
    private Sex sex;
    @Basic
    private LocalDate birthday;

    @Enumerated
    private HairColor hairColor;
    @Enumerated
    private EyeColor eyeColor;
    private Integer height;

    private String instagram;
    private String tiktok;

    @ElementCollection
    @CollectionTable(name = "dancer_styles", joinColumns = @JoinColumn(name = "dancer_id"))
    @Column(name = "styles")
    private Set<DanceStyle> styles = new HashSet<>();

    private OpportunityType opportunityType;

    private String represented;

    private Affiliation affiliation;

    private String ethnicity;

    private Expertise expertise;

    @ElementCollection
    @CollectionTable(name = "dancer_gallery", joinColumns = @JoinColumn(name = "dancer_id"))
    @Column(name = "gallery")
    private List<String> gallery;

    public Dancer(String email, String password) {
        super(email, password);
        this.setRole("DANCER");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dancer dancer = (Dancer) o;
        return getId() != null && Objects.equals(getId(), dancer.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
