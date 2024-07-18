package com.example.jingangfarmmanagement.repository.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "role")
public class Role extends BaseEntity {
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    private String name;
    private Integer type;
    private String description;
}
