package com.example.jingangfarmmanagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticTotalPetFollowFarmRes {
    Long farmId;
    String farmName;
    String farmCode;
    Long totalPet;
    StatisticPetFollowAge statisticPetFollowAge;
    StatisitcPetFollowWeight statisitcPetFollowWeight;
    int quantityRebornInFarm;
    int quantityRebornOutFarm;
    int quantityPetisUilness;
    int quantityPetisnUilness;
    int quantityMale = 0;
    int quantityFemail = 0;
    List<StatisticPetType> statisticPetTypeList;
    List<StatisticPetUilness> statisiticPetUilnessList;

}
