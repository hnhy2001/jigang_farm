package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.BOL;
import com.example.jingangfarmmanagement.service.BOLService;
import com.example.jingangfarmmanagement.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("bol")
public class BOLController extends BaseController<BOL>{
    @Autowired
    private BOLService bolService;
    @Override
    protected BaseService<BOL> getService() {
        return bolService;
    }

    @Override
    @PostMapping("/create")
    public BaseResponse create(@RequestBody BOL t) throws Exception {
        return bolService.customCreate(t);
    }

    @DeleteMapping("/cancel")
    public BaseResponse cancel(@RequestParam Long id) throws Exception {
        return bolService.cancel(id);
    }
}
