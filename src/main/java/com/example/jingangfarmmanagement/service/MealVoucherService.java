package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.entity.MealVoucher;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.springframework.data.domain.Page;

import java.util.List;


public interface MealVoucherService extends BaseService<MealVoucher> {
    public BaseResponse createMealVoucher(MealVoucherReq req);
    public BaseResponse updateMealVoucher(Long mealVoucherId,MealVoucherReq req);
    public Page<MealVoucher> findMealVoucherHistoriesByPet(List<Long> petIds, int page, int size);
}
