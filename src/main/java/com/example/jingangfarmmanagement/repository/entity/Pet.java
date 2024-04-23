package com.example.jingangfarmmanagement.repository.entity;

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

    @Column(name = "type")
    private int type;

    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "cage", nullable = false)
    private Cage cage;

    @Column(name = "healthCondition")
    private int healthCondition;

    @Column(name = "warning")
    private String warning;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "uilness", nullable = true)
    private Uilness uilness;

    @Column(name = "parent")
    private String parent;
}
