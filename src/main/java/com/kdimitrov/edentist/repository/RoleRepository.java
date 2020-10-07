package com.kdimitrov.edentist.repository;

import com.kdimitrov.edentist.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleContains(String remedial);
}
