package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.CageNoteHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CageNoteHistoryRepository extends BaseRepository<CageNoteHistory> {
    @Query("SELECT c FROM CageNoteHistory c WHERE c.createDate = :date AND c.cage.id = :cageId")
    Optional<CageNoteHistory> findByDate(Long date, Long cageId);
}
