package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.CreateBOLReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.BOL;
import com.example.jingangfarmmanagement.service.BOLService;
import com.example.jingangfarmmanagement.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("bol")
public class BOLController{
    @Autowired
    private BOLService bolService;

    @PostMapping("/create")
    public BaseResponse create(@RequestBody CreateBOLReq req) throws Exception {
        return bolService.create(req);
    }

    @PutMapping("/update")
    public BaseResponse upadte(@RequestParam Long id) throws Exception {
        return bolService.update(id);
    }

    @DeleteMapping("/cancel")
    public BaseResponse cancel(@RequestParam Long id) throws Exception {
        return bolService.cancel(id);
    }

    @GetMapping("/search")
    public BaseResponse search(SearchReq req) throws Exception {
        return bolService.search(req);
    }
}
