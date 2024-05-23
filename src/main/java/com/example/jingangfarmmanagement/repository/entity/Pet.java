package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "pet")
@JsonIgnoreProperties({"treatmentCards"})
public class Pet extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "cage", nullable = false)
    private Cage cage;

    @Column(name = "weight")
    private double weight;

    @Column(name = "warning")
    private String warning;

    @Column(name = "note")
    private String note;

    @Column(name = "sex", nullable = false)
    private int sex;

//    @ManyToOne
//    @JoinColumn(name = "uilness", nullable = true)
//    private Uilness uilness;

    @Column(name = "uilness")
    private String uilness;

    @Column(name = "parent")
    private String parent;
    @ManyToMany(mappedBy = "pets")
    @JsonBackReference
    private List<TreatmentCard> treatmentCards;
}
