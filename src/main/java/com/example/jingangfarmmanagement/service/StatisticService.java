package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.StatisticPetWithAge;

public interface StatisticService {
    public BaseResponse statisticPetWithAge(StatisticPetWithAge statisticPetWithAge);
}
