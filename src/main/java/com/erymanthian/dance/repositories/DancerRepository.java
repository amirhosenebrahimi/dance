package com.erymanthian.dance.repositories;

import com.erymanthian.dance.entities.auth.Dancer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DancerRepository extends JpaRepository<Dancer, Long> {
    List<Dancer> findAllByStateAndCity(String state, String city, Pageable pageable);
}
