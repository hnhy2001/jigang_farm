package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentHistoryReq {
    private Long updateDate;
    private String type;
    private String unit;
    private String result;
    private String treatmentCardId;
    List<TreatmentCardMaterialReq> materials;
}
