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
@Table(name = "uilness")
public class Uilness extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "warning_type", nullable = false)
    private UilnessType uilnessType;

    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @CreatedDate
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @LastModifiedDate
    private ZonedDateTime endDate;

    @Column(name = "recoment")
    private String recoment;

}
