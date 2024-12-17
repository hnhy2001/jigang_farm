package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.Uilness;

public interface UilnessService extends BaseService<Uilness>{
    public BaseResponse customeCreate(Uilness uilness) throws Exception;
    public BaseResponse customeUpdate(Uilness uilness) throws Exception;
}
