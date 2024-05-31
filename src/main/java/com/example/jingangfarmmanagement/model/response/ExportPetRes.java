package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportPetRes {
    private Pet pet;
    private String reasonExport;
    private Long exportDate;
    private int type;
    private String note;
}
