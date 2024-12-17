package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.FunctionReq;
import com.example.jingangfarmmanagement.repository.entity.Function;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("function")
public class FunctionController extends BaseController<Function>{
    @Autowired
    private FunctionService functionService;
    @Override
    protected BaseService<Function> getService() {
        return functionService;
    }

    @PostMapping("/custom-create")
    public BaseResponse create(@RequestBody FunctionReq t) throws Exception {
        return functionService.customCreate(t);
    }
}
