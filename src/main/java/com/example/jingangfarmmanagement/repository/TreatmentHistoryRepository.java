package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentHistoryRepository extends BaseRepository<TreatmentHistory> {
    List<TreatmentHistory> findByTreatmentCardId(Long treatmentCardId);
}
