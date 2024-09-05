package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.ImageExportPet;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportPetRes {
    private Long id;
    private Pet pet;
    private String reasonExport;
    private Long exportDate;
    private int type;
    private String note;
    private List<ImageExportPet> images;
}
