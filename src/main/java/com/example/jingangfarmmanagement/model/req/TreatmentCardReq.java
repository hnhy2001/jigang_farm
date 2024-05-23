package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentCardReq {
    String code;
    String note;
    Long createDate;
    List<Long> petIds;
    List<Long> ulinessIds;
    List<TreatmentCardMaterialReq> materials;
}
