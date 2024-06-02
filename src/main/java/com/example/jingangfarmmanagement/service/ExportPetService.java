package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ExportPetReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.response.ExportPetRes;
import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import org.springframework.data.domain.Page;

public interface ExportPetService extends BaseService<ExportPet> {
     BaseResponse createExportPet(ExportPetReq req);
     Page<ExportPetRes> searchExportPet(SearchReq req);
     BaseResponse getExportPetById(Long id);
}
