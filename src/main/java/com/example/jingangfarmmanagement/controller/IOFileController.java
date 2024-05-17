package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.service.IOFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("file")
public class IOFileController {
    @Autowired
    private IOFileService ioFileService;
    @PostMapping("pet/import")
    public BaseResponse importPetFile(@RequestBody MultipartFile file,
                                      @RequestParam(required = true) String farmCode) {
        try {
            ioFileService.importPetsFromExcel(file,farmCode);
            return new BaseResponse(200,"OK",null);
        } catch (IOException e) {
            return new BaseResponse(500,"Error",null);
        }
    }
}
