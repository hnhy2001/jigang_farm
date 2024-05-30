package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.HealthCondition;
import com.example.jingangfarmmanagement.repository.entity.HistoryHealth;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryHealthRepository extends BaseRepository<HistoryHealth>{
    List<HistoryHealth> findByTreatmentHistory(TreatmentHistory treatmentHistory);
}
