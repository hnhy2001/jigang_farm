package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.req.*;
import com.example.jingangfarmmanagement.model.response.UserRes;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.model.BaseResponse;
import org.springframework.data.domain.Page;

import java.security.NoSuchAlgorithmException;

public interface UserService extends BaseService<User> {
    BaseResponse login(LoginRequest loginRequest) throws Exception;
    BaseResponse customCreate(User user) throws Exception;
    BaseResponse changePassword(ChangePasswordReq changePasswordReq);
    User getUserByUsername(String username);
    public BaseResponse customUpdate(String id, UserReq user);
    public Page<UserRes> searchUser(SearchReq req);
    public BaseResponse getUserById(String id);
}
