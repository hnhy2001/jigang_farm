package com.example.jingangfarmmanagement.service;


import com.example.jingangfarmmanagement.model.req.AssignRoleMenuReq;
import com.example.jingangfarmmanagement.repository.entity.Role;

import java.util.List;

public interface RoleService extends BaseService<Role> {
    List<Role> getAllByInId(List<Long> idList);
}
