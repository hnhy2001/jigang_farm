package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.PermissionCageReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.CageProjection;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PermissionCageService {
    public BaseResponse addPermission(PermissionCageReq reqs);
    public BaseResponse updatePermission(PermissionCageReq reqs);
    public BaseResponse removePermission(PermissionCageReq reqs);
    public BaseResponse getPermissionCages(List<Long> userIds);
    public List<Cage> getPermissionCageByUserId(Long userId);
    public BaseResponse getPermissionCageByUserIdAndFarmId(Long farmId);
}
