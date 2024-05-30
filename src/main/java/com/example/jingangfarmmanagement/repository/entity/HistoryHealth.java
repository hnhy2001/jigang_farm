package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryHealth extends BaseEntity{
    private String type;
    private String unit;
    private String result;
    private Long checkingDate;
    @ManyToOne()
    @JoinColumn(name = "treatment_history_id")
    @JsonBackReference
    private TreatmentHistory treatmentHistory;
}
