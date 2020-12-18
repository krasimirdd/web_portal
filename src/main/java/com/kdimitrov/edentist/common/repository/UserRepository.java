package com.kdimitrov.edentist.common.repository;

import com.kdimitrov.edentist.common.model.RoleEntity;
import com.kdimitrov.edentist.common.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByEmail(String email);

    List<UserEntity> findAllByRolesContaining(RoleEntity role);
}
