package com.example.jingangfarmmanagement.model.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatisticQuantityUnilessTypeRes {
    Long totalPet;
    List<StatisticQuantityUnilessTypeItemRes> statisticQuantityUnilessTypeItem;
}
