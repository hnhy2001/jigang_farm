package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.ImageExportPet;
import com.example.jingangfarmmanagement.repository.entity.ImageTreatmentCart;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.ImageExportPetService;
import com.example.jingangfarmmanagement.service.ImageTreatmentCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin
@RestController
@RequestMapping("image_export_pet")
public class ImageExportPetController extends BaseController<ImageExportPet>{
    @Autowired
    private ImageExportPetService imageExportPetService;
    @Override
    protected BaseService<ImageExportPet> getService() {
        return imageExportPetService;
    }

    @PostMapping("upload")
    public BaseResponse handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long exportPetId) {
        return imageExportPetService.uploadFile(file, exportPetId);
    }
}
