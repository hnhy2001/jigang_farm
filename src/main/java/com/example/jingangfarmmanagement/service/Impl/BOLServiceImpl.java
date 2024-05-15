package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BOLRepository;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.entity.BOL;
import com.example.jingangfarmmanagement.repository.entity.MaterialsWarehouse;
import com.example.jingangfarmmanagement.service.BOLService;
import com.example.jingangfarmmanagement.service.MaterialsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BOLServiceImpl extends BaseServiceImpl<BOL> implements BOLService {
    @Autowired
    BOLRepository bolRepository;
    @Autowired
    MaterialsWarehouseService materialsWarehouseService;
    @Override
    protected BaseRepository<BOL> getRepository() {
        return bolRepository;
    }

    @Override
    public BaseResponse customCreate(BOL bol) throws Exception {
        if (bol.getConsignee() == null){
            return new BaseResponse().fail("Người nhận hàng không được để trống");
        }

        if (bol.getWarehouse() == null){
            return new BaseResponse().fail("Kho hàng không được để trống");
        }

        if (bol.getMaterials() == null){
            return new BaseResponse().fail("Nguyên vật liệu không được để trống");
        }

        if (bol.getUnitPridce() <= 0 || bol.getPrice() <= 0 || bol.getActualQuantity() <= 0 || bol.getEstimateQuantity() <= 0){
            return new BaseResponse().fail("Các số liệu phải là số dương");
        }
        BOL result = super.create(bol);
        if (result != null){

        }
        return null;
    }
}
