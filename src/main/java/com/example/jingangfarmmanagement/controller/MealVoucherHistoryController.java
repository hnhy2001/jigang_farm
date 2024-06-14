package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherHistoryReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.repository.entity.MealVoucherHistory;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.MealVoucherHistoryService;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("meal_voucher_history")
public class MealVoucherHistoryController extends BaseController<MealVoucherHistory> {
    @Autowired
    MealVoucherHistoryService mealVoucherHistoryService;

    @Override
    protected BaseService getService() {
        return mealVoucherHistoryService;
    }
    @PostMapping("/voucher/create")
    public BaseResponse createTreatmentHistory(@RequestBody List<MealVoucherHistoryReq> req)  {
        return mealVoucherHistoryService.createMealVoucherHistory(req);
    }

    @PutMapping("/voucher/update")
    public BaseResponse updateTreatmentHistory(@RequestParam Long id,@RequestBody List<MealVoucherHistoryReq> req) {
        return mealVoucherHistoryService.updateMealVoucherHistory(id,req);
    }
    @GetMapping("/voucher/get-by-id")
    public BaseResponse getMealVoucherHistoryById(@RequestParam Long id){
        return mealVoucherHistoryService.getMealVoucherHistoryById(id);
    }
    @GetMapping("/voucher/search")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", mealVoucherHistoryService.searchMealVoucherHistoryHistory(req));
    }

}
