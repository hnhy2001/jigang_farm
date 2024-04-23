package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.repository.entity.UserRole;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.RoleUserRepository;
import com.example.jingangfarmmanagement.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleUserServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {
    @Autowired
    RoleUserRepository roleUserRepository;

    @Override
    protected BaseRepository<UserRole> getRepository() {
        return roleUserRepository;
    }

    @Override
    public List<UserRole> getAllByUserId(Long id) {
        return roleUserRepository.findByUser_IdAndRole_StatusAndStatus(id, Status.ACTIVE, Status.ACTIVE);
    }

    @Override
    public void deleteByUser(Long userId) {
        List<UserRole> userRoles = this.getAllByUserId(userId);
        userRoles.forEach(e -> e.setStatus(Status.DELETED));
        roleUserRepository.saveAll(userRoles);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return this.getAllByUserId(userId).stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
    }
}
