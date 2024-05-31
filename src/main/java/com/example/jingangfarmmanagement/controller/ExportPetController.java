package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ExportPetReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.ExportPetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("export-pet")
public class ExportPetController extends BaseController<ExportPet> {
    @Autowired
    ExportPetService exportPetService;

    @Override
    protected BaseService<ExportPet> getService() {
        return exportPetService;
    }
    @PostMapping("death/create")
    public BaseResponse createExportPet(@RequestBody  ExportPetReq req){
        return exportPetService.createExportPet(req);
    }
    @GetMapping("/death/get-by-id")
    public BaseResponse getExportPetById(@RequestParam Long id){
        return exportPetService.getExportPetById(id);
    }
    @GetMapping("/death/search")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", exportPetService.searchExportPet(req));
    }

}
