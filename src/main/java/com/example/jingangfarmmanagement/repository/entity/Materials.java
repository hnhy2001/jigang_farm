package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "materials")
public class Materials extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "expiration_date")
    private Long expirationDate;

    @Column(name = "unit")
    private String unit;

    @Column(name = "estimateQuantity")
    private Long estimateQuantity;

    @Column(name = "actualQuantity")
    private Long actualQuantity;

    @Column(name = "unitPridce")
    private Long unitPridce;

    @Column(name = "price")
    private Long price;
}

