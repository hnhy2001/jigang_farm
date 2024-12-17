package com.example.jingangfarmmanagement.model.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatisticQuantityUnilessRes {
    Long totalPet;
    List<StatisticQuantityUnilessItemRes> statisticQuantityUnilessItem;
}
