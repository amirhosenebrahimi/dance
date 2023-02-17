package com.erymanthian.dance.repositories;

import com.erymanthian.dance.entities.ConnectRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectRequestRepository extends JpaRepository<ConnectRequest, Long> {

    List<ConnectRequest> findByEventIdAndSourceDancer(Integer targetUser, Long sourceUser);

    List<ConnectRequest> findByEventId(Integer eventId);

}
