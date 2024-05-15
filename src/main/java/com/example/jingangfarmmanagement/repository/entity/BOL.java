package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "bol")
public class BOL extends BaseEntity{
    @Column(name = "type")
    private int type;

    @Column(name = "status")
    private String status;

    @Column(name = "debt")
    private Long debt;

    @ManyToOne
    @JoinColumn(name = "consignee")
    private User consignee;

    @ManyToOne
    @JoinColumn(name = "warehouse")
    private Warehouse warehouse;
}
