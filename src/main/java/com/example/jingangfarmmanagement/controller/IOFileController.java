package com.example.jingangfarmmanagement.controller;

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
    public ResponseEntity<String> importPetFile(@RequestBody MultipartFile file) {
        try {
            ioFileService.importPetsFromExcel(file);
            return ResponseEntity.ok("File uploaded and data imported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to import data from file.");
        }
    }
}
