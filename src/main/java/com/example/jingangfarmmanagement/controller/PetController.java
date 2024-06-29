package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.model.req.UpdateWeightPetReq;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("pet")
public class PetController extends BaseController<Pet> {
    @Autowired
    PetService petService;

    @Override
    protected BaseService<Pet> getService() {
        return petService;
    }

    @PostMapping("change-cage")
    public BaseResponse changeCage(@RequestBody ChangeCageReq changeCageReq) {
        return petService.changeCage(changeCageReq);
    }
    @PostMapping("/create-pet")
    public BaseResponse create(@RequestBody Pet pet) throws Exception {
        return petService.createPet(pet);
    }
    @PutMapping("/update-pet")
    public BaseResponse update(@RequestBody Pet pet) throws Exception {
        return petService.updatePet(pet);
    }
//    @GetMapping("/search")
//    public BaseResponse search(SearchReq req) {
//        return new BaseResponse(200, "Lấy dữ liệu thành công!", petService.customSearch(req));
//    }
    @PutMapping("/update-pet-weight")
    public BaseResponse updatePetWeight(@RequestBody UpdateWeightPetReq updateWeightPet) {
       return petService.updatePetWeight(updateWeightPet);
    }
    @GetMapping("/export-pet")
    public BaseResponse findPetWithCageAndFarm(
                @RequestParam(required = false) Long cageId,
                @RequestParam(required = true) Long farmId,
                @RequestParam(required = false) Long startDate,
                @RequestParam(required = false) Long endDate) {
     return new BaseResponse(200, "Lấy dữ liệu thành công!", petService.findPetWithCageAndFarm(cageId,farmId,startDate,endDate));
   }

}
