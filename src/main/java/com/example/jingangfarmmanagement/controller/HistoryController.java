package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.History;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("history")
public class HistoryController extends BaseController<History>{
    @Autowired
    HistoryService historyService;

    @Override
    protected BaseService<History> getService() {
        return historyService;
    }
}
