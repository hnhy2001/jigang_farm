package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.BOL;

public interface BOLService extends BaseService<BOL>{
    BaseResponse customCreate(BOL bol) throws Exception;
    BaseResponse customUpdate(BOL bol) throws Exception;
    BaseResponse cancel(Long id) throws Exception;
}
