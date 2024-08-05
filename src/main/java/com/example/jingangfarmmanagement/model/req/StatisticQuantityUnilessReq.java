package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Cage;
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
    private int sex;
    private List<Integer> statusList;
    private Cage cage;
    private double fromAge;
    private double toAge;
}
