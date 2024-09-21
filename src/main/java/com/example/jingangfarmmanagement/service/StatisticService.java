package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.StatisticPetWithAge;

public interface StatisticService {
    public BaseResponse statisticPetWithAge(StatisticPetWithAge statisticPetWithAge);
    public BaseResponse statisticByFarm(Long farmId) throws Exception;
    public BaseResponse statisticFarm(SearchReq req,Long userId) throws Exception;
    public BaseResponse statisticCage(SearchReq req,Long userId) throws Exception;
}
