package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "bol")
public class BOL extends BaseEntity{
    @Column(name = "type")
    private int type;

    @Column(name = "debt")
    private Long debt;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "consignee")
    private User consignee;
}
