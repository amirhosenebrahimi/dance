package com.erymanthian.dance.repositories;


import com.erymanthian.dance.entities.Event;
import com.erymanthian.dance.enums.DanceStyle;
import com.erymanthian.dance.enums.PayGrade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCompanyId(Long companyId, Pageable pageable);

    List<Event> findByStateAndCity(String state, String city, Pageable pageable);

    List<Event> findByNameContainsIgnoreCase(String name, Pageable pageable);

    List<Event> findByStylesContains(DanceStyle style, Pageable pageable);

    List<Event> findByPayGradeBetween(PayGrade startPayGrade, PayGrade endPayGrade, Pageable pageable);

    List<Event> findByStateAndCityAndNameContainsIgnoreCase(String state, String city, String name, Pageable pageable);

    List<Event> findByPayGradeBetweenAndStateAndCity(PayGrade startPayGrade, PayGrade endPayGrade, String state, String city, Pageable pageable);

    List<Event> findByPayGradeBetweenAndNameContainsIgnoreCase(PayGrade startPayGrade, PayGrade endPayGrade, String name, Pageable pageable);

    List<Event> findByPayGradeBetweenAndStateAndCityAndNameContainsIgnoreCase(PayGrade startPayGrade, PayGrade endPayGrade, String state, String city, String name, Pageable pageable);


    List<Event> findByStylesContainsAndStateAndCity(DanceStyle styles, String state, String city, Pageable pageable);

    List<Event> findByStylesContainsAndPayGradeBetween(DanceStyle styles, PayGrade startPayGrade, PayGrade endPayGrade, Pageable pageable);
    List<Event> findByStylesContainsAndNameContainsIgnoreCase(DanceStyle styles, String name, Pageable pageable);

    List<Event> findByStylesContainsAndStateAndCityAndNameContainsIgnoreCase(DanceStyle styles, String state, String city, String name, Pageable pageable);

    List<Event> findByStylesContainsAndPayGradeBetweenAndStateAndCity(DanceStyle styles, PayGrade startPayGrade, PayGrade endPayGrade, String state, String city, Pageable pageable);

    List<Event> findByStylesContainsAndPayGradeBetweenAndNameContainsIgnoreCase(DanceStyle styles, PayGrade startPayGrade, PayGrade endPayGrade, String name, Pageable pageable);

    List<Event> findByStylesContainsAndPayGradeBetweenAndStateAndCityAndNameContainsIgnoreCase(DanceStyle styles, PayGrade startPayGrade, PayGrade endPayGrade, String state, String city, String name, Pageable pageable);
}
