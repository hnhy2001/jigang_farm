package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.UpdateFunctionRoleReq;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FunctionRoleService {
    List<FunctionRole> getByRole(Role role);
    BaseResponse update(UpdateFunctionRoleReq req);
    void createAll(List<FunctionRole> req);
    Page<FunctionRole> search(SearchReq req);

}
