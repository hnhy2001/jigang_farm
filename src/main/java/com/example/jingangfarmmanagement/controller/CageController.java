package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    @GetMapping("/search")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", cageService.customSearch(req));
    }
}
