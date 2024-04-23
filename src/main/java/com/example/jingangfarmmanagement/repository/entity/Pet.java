package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "pet")
public class Pet extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false)
    private PetType type;

    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "cage", nullable = false)
    @JsonBackReference
    private Cage cage;

    @ManyToOne
    @JoinColumn(name = "health_condition", nullable = false)
    private HealthCondition healthCondition;

    @Column(name = "warning")
    private String warning;

    @Column(name = "note")
    private String note;

    @Column(name = "sex", nullable = false)
    private int sex;

    @ManyToOne
    @JoinColumn(name = "uilness", nullable = true)
    @JsonBackReference
    private Uilness uilness;

    @Column(name = "parent")
    private String parent;
}
