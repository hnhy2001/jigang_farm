package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.ImageTreatmentCart;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.ImageTreatmentCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin
@RestController
@RequestMapping("image_treatment_cart")
public class ImageTreatmentCartController extends BaseController<ImageTreatmentCart>{
    @Autowired
    private ImageTreatmentCartService imageTreatmentCartService;
    @Override
    protected BaseService<ImageTreatmentCart> getService() {
        return imageTreatmentCartService;
    }

    @PostMapping("upload")
    public BaseResponse handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long treatmentCartId) {
        return imageTreatmentCartService.uploadFile(file, treatmentCartId);
    }
}
