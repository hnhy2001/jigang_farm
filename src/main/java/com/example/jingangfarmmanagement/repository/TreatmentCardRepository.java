package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentCardRepository extends BaseRepository<TreatmentCard> {
    @Query(value = "SELECT tc.* " +
            "FROM treatment_card tc JOIN treatment_card_pet tcp ON tc.id = tcp.treatment_card_id " +
            "JOIN pet p ON p.id = tcp.pet_id " +
            "WHERE p.id IN :petIds",
            countQuery = "SELECT COUNT(*) " +
                    "FROM treatment_card tc JOIN treatment_card_pet tcp ON tc.id = tcp.treatment_card_id " +
                    "JOIN pet p ON p.id = tcp.pet_id " +
                    "WHERE p.id IN :petIds",
            nativeQuery = true)
    Page<TreatmentCard> findTreatmentCardsByPet(@Param("petIds") List<Long> petIds, Pageable pageable);


}
