package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.CageType;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageTypeService;
import com.example.jingangfarmmanagement.service.ChangeCageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("chane-cage/history")
public class ChangeCageHistoryController extends  BaseController<ChangeCageHistory> {
    @Autowired
    ChangeCageHistoryService changeCageHistoryService;
    @Override
    protected BaseService<ChangeCageHistory> getService() {
        return changeCageHistoryService;
    }
}
