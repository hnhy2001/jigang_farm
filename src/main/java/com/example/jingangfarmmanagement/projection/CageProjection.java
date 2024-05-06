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
    FarmWithId getFarm();
//
    interface PetWithId{
        Long getId();
        UilnessWithId getUilness();
        int getStatus();

        interface UilnessWithId{
            Long getId();
            UilnessTypeWithId getUilnessType();

            interface UilnessTypeWithId{
                Long getId();
                int getScore();
            }
        }
    }

    interface FarmWithId{
        Long getId();
    }
//
    interface CageTypeWithId{
        Long getId();
        String getName();
        String getCode();
    }
}
