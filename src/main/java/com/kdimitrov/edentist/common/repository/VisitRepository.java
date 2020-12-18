package com.kdimitrov.edentist.common.repository;

import com.kdimitrov.edentist.common.model.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {

    @Query(value = "SELECT v from VisitEntity v where v.remedial.id =:id and v.approved=false and v.declined=false")
    List<VisitEntity> findByRemedialIdAndApprovedIsFalseAndDeclinedIsFalse(long id);

    @Query(value = "SELECT v from VisitEntity v where v.patient.id =:id")
    List<VisitEntity> findAllByPatientId(long id);

    Optional<VisitEntity> findByCode(String code);

    @Query(value = "SELECT v from VisitEntity v where v.date =:date and v.remedial.email=:remedial")
    Optional<VisitEntity> findAlreadyRequest(LocalDateTime date, String remedial);

    @Query(value = "SELECT v from VisitEntity v where v.date =:date and v.remedial.email=:remedial")
    Optional<VisitEntity> findNextAvailable(LocalDateTime date, String remedial);
}
