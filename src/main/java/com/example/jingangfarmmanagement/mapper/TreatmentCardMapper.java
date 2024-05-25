package com.example.jingangfarmmanagement.mapper;

import com.example.jingangfarmmanagement.model.response.MaterialRes;
import com.example.jingangfarmmanagement.model.response.TreatmentCardRes;
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TreatmentCardMapper {
    TreatmentCardMapper INSTANCE = Mappers.getMapper(TreatmentCardMapper.class);

    TreatmentCardRes toTreatmentCardResponse(TreatmentCard treatmentCard);

    default MaterialRes toMaterialRes(Materials materials, Long quantity) {
        if (materials == null) {
            return null;
        }

        MaterialRes materialRes = new MaterialRes();
        materialRes.setName(materials.getName());
        materialRes.setCode(materials.getCode());
        materialRes.setCargo(materials.getCargo());
        materialRes.setUnit(materials.getUnit());
        materialRes.setPrice(materials.getPrice());
        materialRes.setNote(materials.getNote());
        materialRes.setQuantity(quantity);
        return materialRes;
    }
}