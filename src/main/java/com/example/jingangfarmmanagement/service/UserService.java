package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.req.AssignUserRoleReq;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.LoginRequest;

import java.security.NoSuchAlgorithmException;

public interface UserService extends BaseService<User> {

    BaseResponse login(LoginRequest loginRequest) throws Exception;

    void assignRole(AssignUserRoleReq req) throws Exception;
}
