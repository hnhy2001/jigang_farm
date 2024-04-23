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
@Table(name = "health_condition")
public class HealthCondition extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
