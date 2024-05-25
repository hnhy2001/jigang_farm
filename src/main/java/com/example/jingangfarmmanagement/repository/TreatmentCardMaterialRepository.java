package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCardMaterial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentCardMaterialRepository extends BaseRepository<TreatmentCardMaterial> {
    List<TreatmentCardMaterial> findByTreatmentCardId(Long treatmentCardId);

}
