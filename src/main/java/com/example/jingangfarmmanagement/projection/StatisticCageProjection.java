package com.example.jingangfarmmanagement.projection;

import com.example.jingangfarmmanagement.repository.entity.Pet;

import java.time.ZonedDateTime;
import java.util.List;

public interface StatisticCageProjection {
    Long getId();
    int getStatus();
    String getCode();
    String getName();
    ZonedDateTime getCreateDate();
    ZonedDateTime getUpdateDate();
    String getDescription();
    String getWarning();
    List<Pet> getPetList();
    CageTypeWithId getType();
    interface CageTypeWithId{
        Long getId();
        String getName();
        String getCode();
    }
}
