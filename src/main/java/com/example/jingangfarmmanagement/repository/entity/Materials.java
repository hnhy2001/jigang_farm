package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Column(name="expiration_date")
    private Long expirationDate;

    @Column(name = "unit")
    private String unit;
    @Column(name = "first_inventory")
    private String firstInventory;

    @Column(name = "price")
    private String price;
    @Column(name="note")
    private String note;
    @Column(name="product_type")
    private String productType;
    @Column(name="indications")
    private String indications;
    @Column(name="treatment")
    private String treatment;
    @Column(name="assign")
    private String assign;
//
//    @Transient
//    private Long unitPridce = 0L;
//
//    @Transient
//    private Long price = 0L;
//
//    @Transient
//    private Warehouse warehouse;
}
