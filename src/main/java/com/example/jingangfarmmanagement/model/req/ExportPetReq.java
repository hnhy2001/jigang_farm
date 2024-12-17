package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportPetReq {
    private List<Long> petIds;
    private String reasonExport;
    private Long exportDate;
    private int type;
    private String note;
}
