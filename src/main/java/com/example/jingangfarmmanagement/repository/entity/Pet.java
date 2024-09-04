package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
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
    private String age;

    @Column(name = "birth_number")
    private Long birthNumber;

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
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "pet_uiless",
//            joinColumns = @JoinColumn(name = "pet_id"),
//            inverseJoinColumns = @JoinColumn(name = "uilness_id")
//    )
//    private List<Uilness> uilnesses = new ArrayList<>();
    private String uilness;

    @Column(name = "parent_dad")
    private String parentDad;
    @Column(name = "parent_mom")
    private String parentMon;
    @ManyToMany(mappedBy = "pets")
    @JsonBackReference
    private List<TreatmentCard> treatmentCards;
    @ManyToMany(mappedBy = "pets")
    @JsonIgnore
    private List<MealVoucher> mealVouchers;
    @Column(name = "update_heath_date" )
    private Long updateHeathDate;
    @Column(name = "pet_condition" )
    private int petCondition;
    @Column(name = "last_date_update" )
    private Long lastDateUpdate;
    @Column(name = "pregnant_date_update" )
    private Long pregnantDateUpdate;

}
