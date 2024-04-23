package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("treatment_history")
public class TreatmentHistoryController extends BaseController {
    @Autowired
    TreatmentHistoryService treatmentHistoryService;

    @Override
    protected BaseService getService() {
        return treatmentHistoryService;
    }
}
