package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.CageType;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageTypeService;
import com.example.jingangfarmmanagement.service.ChangeCageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/search/custom")
    public BaseResponse searchChangeCageHistory(@RequestParam(required = false) String cageNameFrom,
                                                @RequestParam(required = false) String cageNameTo,
                                                @RequestParam(required = false) String farmNameFrom,
                                                @RequestParam(required = false) String farmNameTo,
                                                @RequestParam(required = false) Long minDate,
                                                @RequestParam(required = false) Long maxDate,
                                                @RequestParam(required = false) String petName,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {

         Pageable pageable = PageRequest.of(page, size);
            return changeCageHistoryService.searchChangeCageHistoryCustom(
                    cageNameFrom, cageNameTo, farmNameFrom, farmNameTo, minDate, maxDate, petName, pageable);

    }
}
