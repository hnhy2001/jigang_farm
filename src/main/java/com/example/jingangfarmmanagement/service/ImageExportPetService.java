package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.ImageExportPet;
import com.example.jingangfarmmanagement.repository.entity.ImageTreatmentCart;
import org.springframework.web.multipart.MultipartFile;

public interface ImageExportPetService extends BaseService<ImageExportPet> {
    public BaseResponse uploadFile(MultipartFile file, Long id);

}
