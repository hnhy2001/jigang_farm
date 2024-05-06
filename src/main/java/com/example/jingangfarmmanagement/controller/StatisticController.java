package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.service.FarmService;
import com.example.jingangfarmmanagement.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("statisitc")
public class StatisticController {
    @Autowired
    StatisticService statisticService;
    @GetMapping("pet-follow-farm")
    public BaseResponse petFollowFarm() {
        return new BaseResponse(200, "Ok", statisticService.statisticTotalPetFollowFarm());
    }
}
