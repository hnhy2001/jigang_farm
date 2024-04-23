package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.req.AssignUserRoleReq;
import com.example.jingangfarmmanagement.repository.entity.History;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.LoginRequest;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.HistoryService;
import com.example.jingangfarmmanagement.service.UserRoleService;
import com.example.jingangfarmmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;


@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }

    @PostMapping("/role/assign")
    public BaseResponse assignUserRole(@RequestBody AssignUserRoleReq req) throws Exception {
        userService.assignRole(req);
        return new BaseResponse().success();
    }

    @GetMapping("/role")
    public BaseResponse getRoleOfUser(@RequestParam Long userId) {
        return new BaseResponse().success(userRoleService.getRolesByUserId(userId));
    }
}