package com.example.jingangfarmmanagement.repository.entity;

import lombok.*;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
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
