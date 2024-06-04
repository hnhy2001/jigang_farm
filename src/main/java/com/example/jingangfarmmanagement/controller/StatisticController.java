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
@RequestMapping("statistic")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping("farm")
    public BaseResponse statisticWithFarm(SearchReq req) throws Exception {
        return statisticService.statisticFarm(req);
    }

    @GetMapping("cage")
    public BaseResponse statisticWithCage(SearchReq req) throws Exception {
        return statisticService.statisticCage(req);
    }
}
