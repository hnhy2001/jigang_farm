package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.CreateBOLReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.BOL;

public interface BOLService{
//    BaseResponse customCreate(BOL bol) throws Exception;
//    BaseResponse customUpdate(BOL bol) throws Exception;
//    BaseResponse cancel(Long id) throws Exception;
    BaseResponse create(CreateBOLReq req) throws Exception;
    BaseResponse update(Long id) throws Exception;
    BaseResponse cancel(Long id) throws Exception;
    BaseResponse search(SearchReq req);
}
