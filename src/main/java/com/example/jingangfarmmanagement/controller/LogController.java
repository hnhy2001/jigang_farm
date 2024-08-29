package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Log;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageService;
import com.example.jingangfarmmanagement.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("log")
public class LogController extends BaseController<Log>{
    @Autowired
    private LogService logService;

    @Override
    protected BaseService<Log> getService() {
        return logService;
    }

}
