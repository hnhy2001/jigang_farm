package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ExportPetReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.response.ExportPetRes;
import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.ExportPetService;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public BaseResponse searchExportPets(
            @RequestParam Long cageId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer sex,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Long startExportDate,
            @RequestParam(required = false) Long endExportDate,
            @RequestParam(required = false) String note,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExportPetRes> resultPage = exportPetService.searchExportPet(cageId, name, code, sex, age,startExportDate,endExportDate, note, pageable);
        return new BaseResponse(200, "Lấy dữ liệu thành công!",resultPage);
    }
    @GetMapping("death-pet/count")
    public BaseResponse getDeathPetByFarmAndCage(@RequestParam Long cageId){
        return exportPetService.getDeathPetByFarmAndCage(cageId);
    }
}
