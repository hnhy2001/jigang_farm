package com.example.jingangfarmmanagement.model.response;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class StatisticTotalPetFollowFarmRes {
    Long farmId;
    String farmName;
    String farmCode;
    Long totalPet;

}
