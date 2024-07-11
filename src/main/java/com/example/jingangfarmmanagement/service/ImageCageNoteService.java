package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.ImageCageNote;
import org.springframework.web.multipart.MultipartFile;

public interface ImageCageNoteService extends BaseService<ImageCageNote> {
    public BaseResponse uploadFile(MultipartFile file, Long id);

}
