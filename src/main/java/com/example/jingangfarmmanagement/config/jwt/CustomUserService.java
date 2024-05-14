package com.example.jingangfarmmanagement.config.jwt;

import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.repository.UserRepository;
import com.example.jingangfarmmanagement.repository.entity.UserRole;
import com.example.jingangfarmmanagement.service.RoleService;
import com.example.jingangfarmmanagement.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleService userRoleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        List<UserRole> userRoles = userRoleService.getUserRoleByUserId(userOptional.get());
        if (userRoles.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(userOptional.get(), userRoles);
    }
}
