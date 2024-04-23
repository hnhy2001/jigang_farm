package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.repository.entity.Pet;

public interface PetService extends BaseService<Pet> {
    public BaseResponse changeCage(ChangeCageReq changCageReq);
}
