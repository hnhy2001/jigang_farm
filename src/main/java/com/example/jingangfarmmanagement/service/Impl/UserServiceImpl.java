package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.config.jwt.JwtTokenProvider;
import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.req.AssignUserRoleReq;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.LoginResponse;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.model.req.LoginRequest;
import com.example.jingangfarmmanagement.service.*;
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
    private UserRoleService userRoleService;

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
        return new BaseResponse(200, "OK", loginResponse);
    }

    @Transactional
    @Override
    public void assignRole(AssignUserRoleReq req) throws Exception {
        User user = this.getById(req.getUserId());
        userRoleService.deleteByUser(user.getId());
        if (CollectionUtils.isEmpty(req.getRoleIds())) return;

        List<Role> roleList = roleService.getAllByInId(req.getRoleIds());
        if (CollectionUtils.isEmpty(roleList)) return;

        List<UserRole> userRoleList = roleList.stream()
                .map(e -> UserRole.builder()
                        .user(user)
                        .role(e)
                        .build())
                .collect(Collectors.toList());
        userRoleService.createAll(userRoleList);
    }

    private boolean isValidPassword(String userPass, String reqPass) {
        return !StringUtils.isEmpty(reqPass) && passwordEncoder.matches(reqPass, userPass);
    }
}

