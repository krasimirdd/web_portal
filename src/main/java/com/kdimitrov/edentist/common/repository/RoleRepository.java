package com.kdimitrov.edentist.common.repository;

import com.kdimitrov.edentist.common.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleContains(String remedial);
}
