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
@Table(name = "pet_type")
public class PetType extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
