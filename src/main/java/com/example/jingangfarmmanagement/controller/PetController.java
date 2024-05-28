package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.Impl.BaseServiceImpl;
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
}
