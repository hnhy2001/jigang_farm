package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportPet extends BaseEntity{
    private Long petId;
    private String reasonExport;
    private Long exportDate;
    private int type;
    private String note;


}
