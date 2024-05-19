package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "materials")
public class Materials extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "cargo")
    private String cargo;

    @Transient
    private Long expirationDate;

    @Column(name = "unit")
    private String unit;

    @Transient
    private Long estimateQuantity = 0L;

    @Transient
    private Long actualQuantity = 0L;

    @Transient
    private Long unitPridce = 0L;

    @Transient
    private Long price = 0L;

    @Transient
    private Warehouse warehouse;
}
