package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageExportPet extends BaseEntity{
    @Column(name = "url", length = 9999999)
    private String url;

    @ManyToOne
    @JoinColumn(name = "export_pet")
    @JsonBackReference
    private ExportPet exportPet;
}
