package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "materials_bol")
public class MaterialsBOL extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "bol_id")
    private BOL bol;

    @ManyToOne
    @JoinColumn(name = "materials_id")
    private Materials materials;

    @Column(name = "estimate_quantity")
    private Long estimateQuantity = 0L;

    @Column(name = "actual_quantity")
    private Long actualQuantity = 0L;

    @Column(name = "unit_quantity")
    private Long unitPridce = 0L;

    @Column(name = "price")
    private Long price = 0L;

    @Column(name = "expiration_date")
    private Long expirationDate;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
}
