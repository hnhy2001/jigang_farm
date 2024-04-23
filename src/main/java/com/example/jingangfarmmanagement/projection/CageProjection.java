package com.example.jingangfarmmanagement.projection;

import java.time.ZonedDateTime;
import java.util.List;

public interface CageProjection {
    Long getId();
    int getStatus();
    String getCode();
    String getName();
    ZonedDateTime getCreateDate();
    ZonedDateTime getUpdateDate();
    String getDescription();
    String getWarning();
    List<PetWithId> getPetList();
    CageTypeWithId getType();
//
    interface PetWithId{
        Long getId();
        UilnessWithId getUilness();

        interface UilnessWithId{
            Long getId();
        }
    }
//
    interface CageTypeWithId{
        Long getId();
        String getName();
        String getCode();
    }
}
