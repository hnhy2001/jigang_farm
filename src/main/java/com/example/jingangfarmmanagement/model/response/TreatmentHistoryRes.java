package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class TreatmentHistoryRes {
    private List<MaterialRes> material;
    private TreatmentHistory treatmentHistory;
}
