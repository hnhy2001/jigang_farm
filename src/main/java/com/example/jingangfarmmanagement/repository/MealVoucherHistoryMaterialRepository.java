package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.MealVoucherHistoryMaterial;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistoryMaterial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealVoucherHistoryMaterialRepository extends BaseRepository<MealVoucherHistoryMaterial> {
    List<MealVoucherHistoryMaterial> findByMealVoucherHistoryId(Long mealVoucherHistoryId);

}
