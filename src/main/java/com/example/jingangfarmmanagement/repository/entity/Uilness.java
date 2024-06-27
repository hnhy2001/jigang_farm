package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "uilness")
@JsonIgnoreProperties({"treatmentCards"})
public class Uilness extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "score")
    private int score;

    @Column(name = "recoment", length = 1000)
    private String recoment;
    @ManyToMany(mappedBy = "uilnesses")
    @JsonBackReference
    private List<TreatmentCard> treatmentCards;
    @ManyToMany(mappedBy = "uilnesses")
    @JsonBackReference
    private List<Pet> pets;


}
