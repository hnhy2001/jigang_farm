package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangePasswordReq;
import com.example.jingangfarmmanagement.model.req.LoginRequest;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.UserReq;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController extends BaseController<User> {
    @Autowired
    private UserService userService;

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }

    @Override
    @PostMapping("/create")
    public BaseResponse create(@RequestBody User user) throws Exception {
        return userService.customCreate(user);
    }

    @PostMapping("/change-password")
    public BaseResponse changePassword(@RequestBody ChangePasswordReq changePasswordReq) {
        return userService.changePassword(changePasswordReq);
    }

    @PutMapping("/update/custom")
    public BaseResponse customUpdate(@RequestParam String id, @RequestBody UserReq user) {
        return userService.customUpdate(id,user);
    }
    @GetMapping("/search/custom")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", userService.searchUser(req));
    }
    @GetMapping("/detail/custom")
    public BaseResponse getUserById(@RequestParam String id) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", userService.getUserById(id));
    }

}