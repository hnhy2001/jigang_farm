package com.example.jingangfarmmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IOFileService {
    public void importPetsFromExcel(MultipartFile file) throws IOException;
}
