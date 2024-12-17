package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherHistoryReq;
import com.example.jingangfarmmanagement.model.req.MealVoucherReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.entity.MealVoucher;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.MealVoucherService;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("meal_voucher")
public class MealVoucherController extends BaseController<MealVoucher>{
    @Autowired
    MealVoucherService mealVoucherService;
    @Override
    protected BaseService<MealVoucher> getService() {
        return mealVoucherService;
    }
    @PostMapping("/voucher/create")
    public BaseResponse createTreatment(@RequestBody MealVoucherReq req)  {
        return mealVoucherService.createMealVoucher(req);
    }

    @PutMapping("/voucher/update")
    public BaseResponse updateTreatment(@RequestParam Long id,@RequestBody MealVoucherReq req) {
        return mealVoucherService.updateMealVoucher(id,req);
    }
    @GetMapping("pet")
    public BaseResponse findMealVoucherHistoriesByPet(@RequestParam List<Long> petIds,
                                                    @RequestParam int page,
                                                    @RequestParam int size){
        return new BaseResponse(200, "Lấy dữ liệu thành công!", mealVoucherService.findMealVoucherHistoriesByPet(petIds,page,size));
    }
}
