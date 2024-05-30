package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TreatmentHistory extends BaseEntity{
//    @ManyToOne
//    @JoinColumn(name = "pet", nullable = false)
//    private Pet pet;
//
//    @Column(name = "start_date")
//    private Long startDate;
//
//    @Column(name = "end_date")
//    private Long endDate;
//
////    @ManyToOne
////    @JoinColumn(name = "uilness", nullable = false)
////    private Uilness uilness;
//
//    @Column(name = "uilness")
//    private String uilness;
//
//    @Column(name = "infor")
//    private String infor;
//
//    @Column(name = "note")
//    private String note;

    private Long treatmentCardId;
    @OneToMany(mappedBy="treatmentHistory",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<HistoryHealth> historyHealths;
}
