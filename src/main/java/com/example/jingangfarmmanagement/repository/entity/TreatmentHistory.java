package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "treatment_history")
public class TreatmentHistory extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "pet", nullable = false)
    private Pet pet;

    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @CreatedDate
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @LastModifiedDate
    private ZonedDateTime endDate;

//    @ManyToOne
//    @JoinColumn(name = "uilness", nullable = false)
//    private Uilness uilness;

    @Column(name = "uilness")
    private String uilness;

    @Column(name = "type")
    private String type;

    @Column(name = "infor")
    private String infor;

    @Column(name = "note")
    private String note;

}
