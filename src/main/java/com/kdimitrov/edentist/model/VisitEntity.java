package com.kdimitrov.edentist.model;


import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "visits")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @NotNull
    @Column(name = "date", nullable = false, unique = true)
    private LocalDateTime date;

    @Column(name = "visit_code")
    private String code;

    @Column(name = "is_approved")
    private boolean approved;

    @Column(name = "is_declined")
    private boolean declined;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "patient", referencedColumnName = "id")
    private UserEntity patient;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "remedial", referencedColumnName = "id")
    private UserEntity remedial;
}
