package com.example.jingangfarmmanagement.controller;


import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.UilnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("uilness")
public class UilnessController extends BaseController<Uilness>{
    @Autowired
    private UilnessService uilnessService;

    @Override
    protected BaseService<Uilness> getService() {
        return uilnessService;
    }

    @Override
    @PostMapping("/create")
    public BaseResponse create(@RequestBody Uilness t) throws Exception {
        return this.uilnessService.customeCreate(t);
    }

    @Override
    @PutMapping("/update")
    public BaseResponse update(@RequestBody Uilness t) throws Exception {
        return this.uilnessService.customeUpdate(t);
    }
}
