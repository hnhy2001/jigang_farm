package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.FunctionReq;
import com.example.jingangfarmmanagement.repository.entity.Function;

public interface FunctionService extends BaseService<Function>{
    BaseResponse customCreate(FunctionReq functionReq) throws Exception;
}
