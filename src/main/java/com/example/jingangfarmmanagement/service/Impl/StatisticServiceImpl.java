package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.StatisticPetWithAge;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.*;
import com.example.jingangfarmmanagement.uitl.SpecUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class StatisticServiceImpl implements StatisticService {
    @Override
    public BaseResponse statisticPetWithAge(StatisticPetWithAge statisticPetWithAge) {
        return new BaseResponse().success("ok");
    }
}
