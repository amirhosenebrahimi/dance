package com.erymanthian.dance.repositories;


import com.erymanthian.dance.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConnectionId(Long connectionId);
}
