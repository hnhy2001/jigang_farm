package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "functionn")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Function extends BaseEntity {
    @Column(name = "name")
    private String function;

    @Column(name = "action")
    private String action;

    @Column(name = "url")
    private String url;

    @Column(name = "device")
    private String device;
}
