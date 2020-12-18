package com.kdimitrov.edentist.common.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "role", nullable = false)
    private String role;

    public RoleEntity setId(long id) {
        this.id = id;
        return this;
    }

    public RoleEntity setRole(String role) {
        this.role = role;
        return this;
    }
}
