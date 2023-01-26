package com.erymanthian.dance.repositories;

import com.erymanthian.dance.entities.auth.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {}
