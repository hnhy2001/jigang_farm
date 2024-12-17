package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherReq;
import com.example.jingangfarmmanagement.model.req.PermissionCageReq;
import com.example.jingangfarmmanagement.model.response.PermissionCageFarmRes;
import com.example.jingangfarmmanagement.model.response.PermissionCageRes;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.UserRepository;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.PermissionCageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionCageService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CageRepository cageRepository;
    @Override
    public BaseResponse addPermission(PermissionCageReq reqs) {
        try {
             for (Long userId: reqs.getUserId()) {
                 User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + reqs.getUserId()));
                 // Find or throw exception
                 List<Cage> cages = reqs.getCageIds().stream()
                         .map(cageId -> cageRepository.findById(cageId)
                                 .orElseThrow(() -> new RuntimeException("Cage not found with ID: " + cageId))).collect(Collectors.toList());
                 user.setCages(cages);
                 userRepository.save(user);
             }
            return new BaseResponse(200, "OK", null);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    public BaseResponse updatePermission(PermissionCageReq reqs) {
        try {
            for(Long userId: reqs.getUserId()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + reqs.getUserId()));

                List<Cage> cages = reqs.getCageIds().stream()
                        .map(cageId -> cageRepository.findById(cageId)
                                .orElseThrow(() -> new RuntimeException("Cage not found with ID: " + cageId)))
                        .collect(Collectors.toList());

                user.setCages(cages);
                userRepository.save(user);
            }
            return new BaseResponse(200, "Permissions updated successfully", null);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    public BaseResponse removePermission(PermissionCageReq permissionCageReq) {
        try {
            for (var userId : permissionCageReq.getUserId()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

//                List<Cage> cagesToRemove = user.getCages().stream()
//                        .map(cage -> cageRepository.findById(cage.getId())
//                                .orElseThrow(() -> new RuntimeException("Cage not found with ID: " + cage)))
//                        .collect(Collectors.toList());
                List<Cage> cagesToRemove = user.getCages().stream().filter(cage -> permissionCageReq.getCageIds().contains(cage.getId())).collect(Collectors.toList());
                user.getCages().removeAll(cagesToRemove);
                userRepository.save(user);
            }
            return new BaseResponse(200, "Permissions removed successfully", null);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    public BaseResponse getPermissionCages(List<Long> userIds) {
        try {
            List<PermissionCageRes> permissionCageResList =new ArrayList<>();
            for(var userId:userIds){
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

                List<Cage> cages = user.getCages();
                PermissionCageRes permissionCageRes = new PermissionCageRes();
                permissionCageRes.setUserId(userId);
                permissionCageRes.setCages(cages);
                permissionCageResList.add(permissionCageRes);
            }
            return new BaseResponse(200, "Permissions retrieved successfully", permissionCageResList);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    public List<Cage> getPermissionCageByUserId(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            return user.getCages();

        } catch (Exception e) {
         throw  new RuntimeException();
        }
    }
    @Override
    public BaseResponse getPermissionCageByUserIdAndFarmId(Long farmId) {
        try {
            List<Cage> cages = cageRepository.findByFarmId(farmId);
            List<PermissionCageFarmRes> result =new ArrayList<>();
            for(var cage : cages){
                List<Long> users = cage.getUsers().stream().map(User::getId).collect(Collectors.toList());
                PermissionCageFarmRes permissionCageFarmRes = new PermissionCageFarmRes();
                permissionCageFarmRes.setCage(cage);
                permissionCageFarmRes.setUserIds(users);
                result.add(permissionCageFarmRes);
            }
            return new BaseResponse(200, "Permissions retrieved successfully", result);
        } catch (Exception e) {
            throw  new RuntimeException();
        }
    }


}
