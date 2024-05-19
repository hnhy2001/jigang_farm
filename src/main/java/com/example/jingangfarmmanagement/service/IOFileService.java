package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IOFileService {
    public BaseResponse importPetsFromExcel(MultipartFile file,String farmCode,String cageCode) throws IOException;
    public BaseResponse importMaterialsFromExcel(MultipartFile file) throws IOException;
}
