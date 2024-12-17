package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

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
    @OneToMany(mappedBy = "exportPet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageExportPet> images;

}
