package com.example.jingangfarmmanagement.projection;

import lombok.Data;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.List;

public interface FarmProjection {
    Long getId();
    int getStatus();
    String getCode();
    String getName();
//    int getType();
    ZonedDateTime getCreateDate();
    ZonedDateTime getUpdateDate();
    String getDescription();
    List<CageWithId> getCageList();
    FarmTypeWithId getFarmType();
    interface CageWithId{
        Long getId();
        int getStatus();
    }

    interface FarmTypeWithId{
        Long getId();
        String getName();
        String getCode();
    }
}
