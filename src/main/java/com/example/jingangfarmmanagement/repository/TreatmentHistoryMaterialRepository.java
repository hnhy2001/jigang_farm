package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentHistoryMaterial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentHistoryMaterialRepository extends BaseRepository<TreatmentHistoryMaterial> {
    List<TreatmentHistoryMaterial> findByTreatmentHistoryId(Long treatmentHistoryId);

}
