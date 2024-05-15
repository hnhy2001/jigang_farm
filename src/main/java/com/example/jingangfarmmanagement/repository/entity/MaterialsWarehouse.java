package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "materials_warehouse")
public class MaterialsWarehouse extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "materials_id")
    private Materials materials;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "quatity")
    private Long quatity;
}
