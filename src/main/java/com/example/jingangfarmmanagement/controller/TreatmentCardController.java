package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("treatment_card")
public class TreatmentCardController extends BaseController<TreatmentCard>{
    @Autowired
    TreatmentCardService treatmentCardService;
    @Override
    protected BaseService<TreatmentCard> getService() {
        return treatmentCardService;
    }
    @PostMapping("/treatment/create")
    public BaseResponse createTreatment(@RequestBody TreatmentCardReq req)  {
        return treatmentCardService.createTreatment(req);
    }

    @PutMapping("/treatment/update")
    public BaseResponse updateTreatment(@RequestParam Long id,@RequestBody TreatmentCardReq req) {
        return treatmentCardService.updateTreatment(id,req);
    }
    @GetMapping("pet")
    public BaseResponse findTreatmentHistoriesByPet(@RequestParam List<Long> petIds,
                                                    @RequestParam int page,
                                                    @RequestParam int size){
        return new BaseResponse(200, "Lấy dữ liệu thành công!", treatmentCardService.findTreatmentHistoriesByPet(petIds,page,size));
    }
}
