package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherHistoryReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.model.response.MealVoucherHistoryRes;
import com.example.jingangfarmmanagement.model.response.TreatmentHistoryRes;
import com.example.jingangfarmmanagement.repository.entity.MealVoucherHistory;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MealVoucherHistoryService extends BaseService<MealVoucherHistory>{
    public BaseResponse createMealVoucherHistory(List<MealVoucherHistoryReq> req);
    public BaseResponse updateMealVoucherHistory(Long mealVoucherHistoryId, List<MealVoucherHistoryReq> reqList);
    public BaseResponse getMealVoucherHistoryById(Long id);
    public Page<MealVoucherHistoryRes> searchMealVoucherHistoryHistory(SearchReq req);

}
