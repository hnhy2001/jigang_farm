package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.config.jwt.JwtTokenProvider;
import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.req.*;
import com.example.jingangfarmmanagement.model.response.MaterialRes;
import com.example.jingangfarmmanagement.model.response.TreatmentHistoryRes;
import com.example.jingangfarmmanagement.model.response.UserRes;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.LoginResponse;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import com.example.jingangfarmmanagement.service.*;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private static final String DELETED_FILTER =";status>-1" ;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private FunctionRoleService functionRoleService;

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
        if (userOptional.isEmpty())
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
        loginResponse.setUserId(user.getId());
        loginResponse.setFunctions(getListFunction(user));
        List<Role> role = userRoleRepository.findAllByUser(user).stream().map(UserRole::getRole).collect(Collectors.toList());
        loginResponse.setRole(role);
        return new BaseResponse(200, "OK", loginResponse);
    }

    List<Function> getListFunction(User user){
        List<UserRole> roleList = userRoleService.getUserRoleByUserId(user);
        List<Function> result = new ArrayList<>();
        roleList.stream().forEach(e -> {
            List<FunctionRole> functionRoleList = functionRoleService.getByRole(e.getRole());
            functionRoleList.stream().forEach(functionRole -> {
                if(functionRole.getStatus() == 1){
                    result.add(functionRole.getFunction());

                }
            });
        });
        return result;
    }
    @Override
    public BaseResponse customCreate(User user) throws Exception {
        User result = new User();
        try {
            if (user.getUserName() == null) {
                return new BaseResponse().fail("Tài khoản không được để trống");
            }
            if (user.getPassword() == null) {
                return new BaseResponse().fail("Mật khẩu không được để trống");
            }

            if (userRepository.findByUserName(user.getUserName()).isPresent()) {
                return new BaseResponse().fail("Tài khoản đã tồn tại");
            }
            user.setCreateDate(DateUtil.getCurrenDateTime());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            List<UserRole> userRoles = new ArrayList<>();
            result = super.create(user);
            User finalResult = result;
            result.getRole().forEach(e -> {
                UserRole userRole = new UserRole();
                userRole.setUser(finalResult);
                userRole.setRole(e);
                userRoles.add(userRole);
            });
            userRoleRepository.saveAll(userRoles);
            logService.logAction(ELogType.CREATE_USER,
                    "Tạo thông tin tài khoản " + result.getUserName() + " thành công" ,
                    "success");
            return new BaseResponse().success(result);
        }catch (Exception e){
            logger.error("Error occurred while create account: {}", e.getMessage(), e);
            logService.logAction(ELogType.CREATE_USER,
                    "Tạo thông tin tài khoản  " + result.getUserName() + " thất bại" + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Có lỗi xảy ra khi tạo tài khoản", null);
        }
    }

    @Override
    public BaseResponse customUpdate(Long id, UserReq user) {
        try {
            if (user.getUserName() == null) {
                return new BaseResponse().fail("Tài khoản không được để trống");
            }

            Optional<User> userExistOptional = userRepository.findById(id);
            if (userExistOptional.isEmpty()) {
                return new BaseResponse().fail("Không tồn tại người dùng");
            }
            User existingUser = userExistOptional.get();
            existingUser.setUpdateDate(DateUtil.getCurrenDateTime());
            existingUser.setUserName(user.getUserName());
            existingUser.setFullName(user.getFullName());
            existingUser.setAddress(user.getAddress());
            existingUser.setEmail(user.getEmail());


            List<UserRole> existingUserRoles = userRoleRepository.findAllByUser(existingUser);
            userRoleRepository.deleteAllInBatch(existingUserRoles);
            List<UserRole> newUserRoles = new ArrayList<>();
            for (var role : user.getRoleId()) {
                UserRole userRole = new UserRole();
                userRole.setUser(existingUser);
                userRole.setRole(roleRepository.findById(role).get());
                newUserRoles.add(userRole);
            }
            userRoleRepository.saveAllAndFlush(newUserRoles);
            userRepository.saveAndFlush(existingUser);
            logService.logAction(ELogType.UPDATE_USER,
                    "Cập nhật thông tin tài khoản " + user.getUserName() + " thành công" ,
                    "success");
            return new BaseResponse().success(existingUser);
        }catch (Exception e){
            logger.error("Error occurred while create account: {}", e.getMessage(), e);
            logService.logAction(ELogType.UPDATE_USER,
                    "Cập nhật thông tin tài khoản  " + user.getUserName() + " thất bại" + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Có lỗi xảy ra khi tạo tài khoản", null);
        }
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
    @Override
    public Page<UserRes> searchUser(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<User> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        Page<User> users = userRepository.findAll(spec, pageable);
        List<UserRes> userResList= new ArrayList<>();
        for(var user : users){
            UserRes userRes =new UserRes();
            userRes.setId(user.getId());
            userRes.setUserName(user.getUserName());
            userRes.setFullName(user.getFullName());
            userRes.setAddress(user.getAddress());
            userRes.setEmail(user.getEmail());
            userRes.setStatus(user.getStatus());
            List<UserRole> existingUserRoles = userRoleRepository.findAllByUser(user);
            List<Role> roles = !existingUserRoles.isEmpty() ? existingUserRoles.stream().map(UserRole::getRole).collect(Collectors.toList()) : null;
            userRes.setRole(roles);
            userResList.add(userRes);
        }


        return new PageImpl<>(userResList, pageable, users.getTotalElements());
    }
    @Override
    public BaseResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserRes userRes =new UserRes();
        userRes.setId(user.getId());
        userRes.setUserName(user.getUserName());
        userRes.setFullName(user.getFullName());
        userRes.setAddress(user.getAddress());
        userRes.setEmail(user.getEmail());
        userRes.setStatus(user.getStatus());
        List<UserRole> existingUserRoles = userRoleRepository.findAllByUser(user);
        List<Role> roles = !existingUserRoles.isEmpty() ? existingUserRoles.stream().map(UserRole::getRole).collect(Collectors.toList()) : null;
        userRes.setRole(roles);
        return new BaseResponse(200, "OK", userRes);
    }
}

