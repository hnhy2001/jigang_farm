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
    public BaseResponse importPetFile(@RequestBody MultipartFile file) throws IOException {

        return ioFileService.importPetsFromExcel(file);

    }
    @PostMapping("material/import")
    public BaseResponse importMaterialFile(@RequestBody MultipartFile file) throws IOException {

            return ioFileService.importMaterialsFromExcel(file);

    }
}
