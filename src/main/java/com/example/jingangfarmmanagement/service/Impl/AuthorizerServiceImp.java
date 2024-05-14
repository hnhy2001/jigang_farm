package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.config.jwt.CustomUserDetails;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.repository.entity.UserRole;
import com.example.jingangfarmmanagement.service.AuthorizerService;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import com.example.jingangfarmmanagement.service.UserRoleService;
import com.example.jingangfarmmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import java.lang.annotation.Annotation;
import java.util.*;

@Service("appAuthorizer")
public class AuthorizerServiceImp implements AuthorizerService {
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    FunctionRoleService functionRoleService;
    @Override
    public boolean authorize(Authentication authentication, String action, Object callerObj) throws Exception {
        String securedPath = extractSecuredPath(callerObj);
        if (securedPath==null || "".equals(securedPath.trim())) {//login, logout
            return true;
        }
//        String menuCode = securedPath.substring(1);//Bỏ dấu "/" ở đầu Path
        boolean isAllow = false;
        try {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authentication;
            if (user==null){
                return isAllow;
            }
            CustomUserDetails customUserDetails = (CustomUserDetails) user.getPrincipal();
//            String userId = (String)user.getPrincipal();
//            if (userId==null || "".equals(userId.trim())) {
//                return isAllow;
//            }
//            User userCheck = userService.getUserByUsername("hnhy01");
//            if (userCheck==null) {
//                return isAllow;
//            }
//            List<UserRole> userRoles = userRoleService.getUserRoleByUserId(userCheck);
            List<FunctionRole> functionRoles = functionRoleService.getByRole(customUserDetails.getUserRoles().get(0).getRole());
            if (functionRoles.get(0).getFunction().getFunction().equals(securedPath)){
                isAllow = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return isAllow;
    }

    private String extractSecuredPath(Object callerObj) {
        Class<?> clazz = ResolvableType.forClass(callerObj.getClass()).getRawClass();
        Optional<Annotation> annotation = Arrays.asList(clazz.getAnnotations()).stream().filter((ann) -> {
            return ann instanceof RequestMapping;
        }).findFirst();
        if (annotation.isPresent()) {
            return ((RequestMapping) annotation.get()).value()[0];
        }
        return null;
    }
}
