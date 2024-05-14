package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.config.jwt.JwtTokenProvider;
import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.req.AssignUserRoleReq;
import com.example.jingangfarmmanagement.model.req.ChangePasswordReq;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.LoginResponse;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.model.req.LoginRequest;
import com.example.jingangfarmmanagement.service.*;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public User create(User user) throws Exception {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.create(user);
    }


    @Override
    public BaseResponse login(LoginRequest loginRequest) throws Exception {

        Optional<User> userOptional = userRepository.findByUserName(loginRequest.getUserName());
        if (!userOptional.isPresent())
            return new BaseResponse(500, "Account không tồn tại", null);

        User user = userOptional.get();
        if (!Objects.equals(user.getStatus(), Status.ACTIVE))
            return new BaseResponse(500, "Account đã bị khóa", null);

        if (!isValidPassword(user.getPassword(), loginRequest.getPassword())) {
            return new BaseResponse(500, "Mật khẩu không chính xác", null);
        }
        History history = new History();
        history.setUser(userOptional.get());
        history.setDescription("Login");
        historyService.create(history);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtTokenProvider.generateToken(user.getUserName()));
        loginResponse.setUserName(user.getUserName());
        loginResponse.setFullName(user.getFullName());
        return new BaseResponse(200, "OK", loginResponse);
    }

    @Override
    public BaseResponse customCreate(User user) throws Exception {
        if (user.getUserName() == null){
            return new BaseResponse().fail("Tài khoản không được để trống");
        }
        if (user.getPassword() == null){
            return new BaseResponse().fail("Mật khẩu không được để trống");
        }

        if (userRepository.findByUserName(user.getUserName()).isPresent()){
            return new BaseResponse().fail("Tài khoản đã tồn tại");
        }
        user.setCreateDate(DateUtil.getCurrenDateTime());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new BaseResponse().success(super.create(user));
    }

    @Override
    public BaseResponse changePassword(ChangePasswordReq changePasswordReq) {
        User user = userRepository.findAllById(changePasswordReq.getUserId());
        if (user == null){
            return new BaseResponse().fail("Tài khoản không tồn tại");
        }
        user.setPassword(passwordEncoder.encode(changePasswordReq.getNewPassword()));
        userRepository.save(user);
        return new BaseResponse().success("Thay đổi mật khẩu thành công");
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).get();
    }

    private boolean isValidPassword(String userPass, String reqPass) {
        return !StringUtils.isEmpty(reqPass) && passwordEncoder.matches(reqPass, userPass);
    }
}

