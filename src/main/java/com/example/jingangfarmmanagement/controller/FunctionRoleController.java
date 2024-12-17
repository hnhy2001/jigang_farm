package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.UpdateFunctionRoleReq;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.HealthCondition;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("function_role")
public class FunctionRoleController{
    @Autowired
    FunctionRoleService functionRoleService;

    @PutMapping("update")
    public BaseResponse update(@RequestBody UpdateFunctionRoleReq req){
        return functionRoleService.update(req);
    }

    @GetMapping("/search")
//    @PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", functionRoleService.search(req));
    }
}
