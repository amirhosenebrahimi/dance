package com.erymanthian.dance.entities;

import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.enums.DanceStyle;
import com.erymanthian.dance.enums.OpportunityType;
import com.erymanthian.dance.enums.PayGrade;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long companyId;
    private String name;
    private String state;
    private String city;
    private String street;
    private String street2;
    private Integer zip;

    /**
     * EVENT
     * 0   NAME
     * 1   DESCRIPTION
     * 2   STYLE PAY-GRADE
     * 3   OPPORTUNITY
     * 4   IMAGE
     * 5   PUBLISHED
     */

    private int step;
    private String description;
    @ElementCollection
    @CollectionTable(name = "event_styles", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "styles")
    private Set<DanceStyle> styles = new HashSet<>();
    private PayGrade payGrade;
    @ElementCollection
    @CollectionTable(name = "event_opportunities", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "opportunities")
    private Set<OpportunityType> opportunities;

    @ElementCollection
    @CollectionTable(name = "event_gallery", joinColumns = @JoinColumn(name = "event_id"))
    //TODO: can we remove this?
    @Column(name = "gallery")
    private List<String> gallery;

//    @ToString.Exclude
//    @OneToMany(cascade = CascadeType.ALL)
//    private Set<Dancer> dancers = new HashSet<>();

    public Event(Long companyId, String name, String state, String city) {
        this.companyId = companyId;
        this.name = name;
        this.state = state;
        this.city = city;
        step = 0;
    }

    public Event(Long companyId, String name, String state, String city, String street, String street2, Integer zip) {
        this.companyId = companyId;
        this.name = name;
        this.state = state;
        this.city = city;
        this.street = street;
        this.street2 = street2;
        this.zip = zip;
        this.step = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
