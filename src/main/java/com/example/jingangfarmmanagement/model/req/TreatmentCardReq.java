package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
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
    int resultTypeCard;
    long resultTypeCardDate;
    List<Long> petIds;
    List<String> ulinessName;
}
