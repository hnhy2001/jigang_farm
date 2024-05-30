package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentHistoryRepository extends BaseRepository<TreatmentHistory> {
    @Query(value = "SELECT th.* \n" +
            "FROM treatment_card tc JOIN treatment_card_pet tcp ON tc.id = tcp.treatment_card_id \n" +
            "JOIN treatment_history th ON th.treatment_card_id = tc.id\n" +
            "JOIN pet p ON p.id = tcp.pet_id \n" +
            "WHERE p.id IN :petIds",
            countQuery = "SELECT COUNT(th.*) \n" +
                    "FROM treatment_card tc JOIN treatment_card_pet tcp ON tc.id = tcp.treatment_card_id \n" +
                    "JOIN treatment_history th ON th.treatment_card_id = tc.id\n" +
                    "JOIN pet p ON p.id = tcp.pet_id \n" +
                    "WHERE p.id IN :petIds",
            nativeQuery = true)
    Page<TreatmentHistory> findTreatmentHistoriesByPet(List<Long> petIds, Pageable pageable);
}
