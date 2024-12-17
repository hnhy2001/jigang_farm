package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.MealVoucherHistory;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealVoucherHistoryRepository extends BaseRepository<MealVoucherHistory> {
    List<MealVoucherHistory> findByMealVoucherId(Long mealVoucherId);
}
