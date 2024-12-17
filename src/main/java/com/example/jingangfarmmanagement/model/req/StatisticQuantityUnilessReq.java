package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import io.swagger.models.auth.In;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatisticQuantityUnilessReq {
    private String startDate;
    private String endDate;
    private List<Integer> sex;
    private List<Integer> statusList;
    private List<Integer> cage;
    private double fromAge;
    private double toAge;
    private List<Integer> farm;
    private double fromWeight;
    private double toWeight;
}
