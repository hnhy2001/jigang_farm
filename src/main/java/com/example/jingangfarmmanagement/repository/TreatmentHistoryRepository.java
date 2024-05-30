package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentHistoryRepository extends BaseRepository<TreatmentHistory> {
    List<TreatmentHistory> findByTreatmentCardId(Long treatmentCardId);
}
