package com.kdimitrov.edentist.repository;

import com.kdimitrov.edentist.model.VisitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {

    @Query(value = "SELECT v from VisitEntity v where v.remedial.id =:id and v.approved=false and v.declined=false")
    List<VisitEntity> findByRemedialIdAndApprovedIsFalseAndDeclinedIsFalse(long id);

    @Query(value = "SELECT v from VisitEntity v where v.patient.id =:id")
    List<VisitEntity> findAllByPatientId(long id);

    Optional<VisitEntity> findByCode(String code);

}
