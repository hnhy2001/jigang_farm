package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.ImageTreatmentCart;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.ImageCageNoteService;
import com.example.jingangfarmmanagement.service.ImageTreatmentCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("image-cage-note")
public class ImageCageNoteController extends BaseController{
    @Autowired
    private ImageCageNoteService imageCageNoteService;

    @PostMapping("upload")
    public BaseResponse handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long cageNoteId) {
        return imageCageNoteService.uploadFile(file, cageNoteId);
    }

    @Override
    protected BaseService getService() {
        return imageCageNoteService;
    }
}
