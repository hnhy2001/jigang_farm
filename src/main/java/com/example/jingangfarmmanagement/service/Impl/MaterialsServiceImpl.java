package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.MaterialsRepository;
import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.service.MaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialsServiceImpl extends BaseServiceImpl<Materials> implements MaterialsService {
    @Autowired
    MaterialsRepository materialsRepository;
    @Autowired
    LogServiceImpl logService;

    @Override
    protected BaseRepository<Materials> getRepository() {
        return materialsRepository;
    }

    @Override
    public List<Materials> createMaterials(List<Materials> materials) {
        try {
            logService.logAction(ELogType.CREATE_MATERIAL,
                    "Tạo thông tin vật tư " +materials + " thành công",
                    "success");
            return materialsRepository.saveAll(materials);

        }catch (Exception e){
            logService.logAction(ELogType.CREATE_MATERIAL,
                    "Tạo thông tin vật tư " +materials + " thất bại" + e.getMessage(),
                    "fail");
            throw e;
        }
    }
}
