package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("treatment_history")
public class TreatmentHistoryController extends BaseController<TreatmentHistory> {
    @Autowired
    TreatmentHistoryService treatmentHistoryService;

    @Override
    protected BaseService getService() {
        return treatmentHistoryService;
    }
    @PostMapping("/treatment/create")
    public BaseResponse createTreatmentHistory(@RequestBody List<TreatmentHistoryReq> req)  {
        return treatmentHistoryService.createTreatmentHistory(req);
    }

    @PutMapping("/treatment/update")
    public BaseResponse updateTreatmentHistory(@RequestParam Long id,@RequestBody List<TreatmentHistoryReq> req) {
        return treatmentHistoryService.updateTreatmentHistory(id,req);
    }
    @GetMapping("/treatment/get-by-id")
    public BaseResponse getTreatmentHistoryById(@RequestParam Long id){
        return treatmentHistoryService.getTreatmentHistoryById(id);
    }
    @GetMapping("/treatment/search")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", treatmentHistoryService.searchTreatmentHistory(req));
    }
    @GetMapping("pet")
    public BaseResponse findTreatmentHistoriesByPet(@RequestParam  List<Long> petIds,
                                                              @RequestParam int page,
                                                              @RequestParam int size){
        return new BaseResponse(200, "Lấy dữ liệu thành công!", treatmentHistoryService.findTreatmentHistoriesByPet(petIds,page,size));
    }
}
