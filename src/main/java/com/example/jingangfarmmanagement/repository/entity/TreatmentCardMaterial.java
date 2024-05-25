package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TreatmentCardMaterial extends BaseEntity{

//    @ManyToOne()
//    @JoinColumn(name="treatment_card_id")
//    private TreatmentCard treatmentCard;
//    @ManyToOne()
//    @JoinColumn(name="material_id")
//    private Materials material;
    private Long treatmentCardId;
    private Long materialId;
    private Long quantity;
}
