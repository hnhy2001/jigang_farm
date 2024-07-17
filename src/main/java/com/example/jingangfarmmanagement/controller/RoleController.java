package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.AssignRoleMenuReq;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("role")
public class RoleController extends BaseController<Role> {
    @Autowired
    private RoleService roleService;

    @Override
    protected BaseService<Role> getService() {
        return roleService;
    }

    @PostMapping("/menu/assign")
    BaseResponse assignRoleMenu(@RequestBody AssignRoleMenuReq req) throws Exception {
        return new BaseResponse().success();
    }
}