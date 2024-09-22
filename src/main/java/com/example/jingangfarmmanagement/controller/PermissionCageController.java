package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.PermissionCageReq;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FarmService;
import com.example.jingangfarmmanagement.service.PermissionCageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("permission_cage")
public class PermissionCageController  {
    @Autowired
    PermissionCageService permissionCageService;

    @PostMapping("/add")
    public BaseResponse addPermission(@RequestBody PermissionCageReq reqs) {
        return permissionCageService.addPermission(reqs);
    }

    // Update permissions
    @PutMapping("/update")
    public BaseResponse updatePermission(@RequestBody PermissionCageReq reqs) {
        return permissionCageService.updatePermission(reqs);
    }

    // Remove permissions
    @DeleteMapping("/remove")
    public BaseResponse removePermission(@RequestBody PermissionCageReq reqs) {
        return permissionCageService.removePermission(reqs);
    }


    // Get permissions for a list of user IDs
    @PostMapping("/get")
    public BaseResponse getPermissionCages(@RequestBody List<Long> userIds) {
        return permissionCageService.getPermissionCages(userIds);
    }

    // Get permissions for a specific user ID
    @GetMapping("/user/{userId}")
    public List<Cage> getPermissionCageByUserId(@PathVariable Long userId) {
        return permissionCageService.getPermissionCageByUserId(userId);
    }
    @GetMapping("/user/farm")
    public BaseResponse getPermissionCageByUserIdAndFarmId(@RequestParam Long farmId) {
        return permissionCageService.getPermissionCageByUserIdAndFarmId(farmId);
    }

}

