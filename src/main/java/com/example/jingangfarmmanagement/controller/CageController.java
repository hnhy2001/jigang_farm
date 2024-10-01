package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("cage")
public class CageController extends BaseController<Cage>{
    @Autowired
    private CageService cageService;

    @Override
    protected BaseService<Cage> getService() {
        return cageService;
    }

    @GetMapping("get-quantity-pet")
    public BaseResponse getQuantityPet(){
        return cageService.quantityPet();
    }
    @GetMapping("/custom/search")
    public Page<Cage> customSearch(@RequestParam String filter,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam String sort,
                                   @RequestParam(required = false) Long userId) {
        SearchReq req = new SearchReq();
        req.setFilter(filter);
        req.setPage(page);
        req.setSize(size);
        req.setSort(sort);

        return cageService.customSearch(req, userId);
    }
}
