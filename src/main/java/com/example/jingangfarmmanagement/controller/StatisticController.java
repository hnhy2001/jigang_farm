package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.StatisticPetWithAge;
import com.example.jingangfarmmanagement.service.FarmService;
import com.example.jingangfarmmanagement.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("statisic")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @PostMapping("pet-with-age")
    public BaseResponse statisticPetWithAge(@RequestBody StatisticPetWithAge statisticPetWithAge){
        return statisticService.statisticPetWithAge(statisticPetWithAge);
    }
}
