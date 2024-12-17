package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.HistoryHealth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentHistoryReq {
    private List<HistoryHealthReq> historyHealths;
    private Long treatmentCardId;
    private Long checkingDate;
    List<TreatmentCardMaterialReq> materials;
    private String updatedBy;
}
