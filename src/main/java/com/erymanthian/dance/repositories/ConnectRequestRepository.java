package com.erymanthian.dance.repositories;

import com.erymanthian.dance.entities.ConnectRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectRequestRepository extends JpaRepository<ConnectRequest, Long> {

    List<ConnectRequest> findByEventIdAndSourceDancer(Integer targetUser, Long sourceUser);

    List<ConnectRequest> findByEventId(Integer eventId);

    @Query(value = "select new ConnectRequest(cr.id,cr.eventId,cr.sourceDancer,cr.status,c.businessName,e.name) from ConnectRequest cr " +
            "left join Event e on e.id=cr.eventId " +
            "left join Company c on c.id=e.companyId where cr.sourceDancer= :dancer")
    List<ConnectRequest> findBySourceDancer(@Param("dancer") Long id);

    @Query(value = "select new ConnectRequest(cr.id,cr.eventId,cr.sourceDancer,cr.status,d.firstName,e.name,d.lastName,d.image) from ConnectRequest cr " +
            "left join Event e on e.id=cr.eventId " +
            "left join Dancer d on d.id=cr.sourceDancer where e.companyId= :company")
    List<ConnectRequest> findByCompany(@Param("company") Long id);
}
