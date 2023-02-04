package com.erymanthian.dance.repositories;

import com.erymanthian.dance.entities.ConnectRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectRequestRepository extends JpaRepository<ConnectRequest, Long> {

    List<ConnectRequest> findByTargetDancerAndSourceDancerAndStatusLessThan(Long targetUser, Long sourceUser, Short status);

    List<ConnectRequest> findByTargetDancerOrSourceDancer(Long targetDancer, Long sourceDancer);

}
