package com.example.jingangfarmmanagement.projection;

import java.time.ZonedDateTime;
import java.util.List;

public interface StatisticFarmProjection {
    Long getId();
    int getStatus();
    String getCode();
    String getName();
    //    int getType();
    ZonedDateTime getCreateDate();
    ZonedDateTime getUpdateDate();
    String getDescription();
    List<StatisticCageProjection> getCageList();
    FarmTypeWithId getFarmType();

    interface FarmTypeWithId{
        Long getId();
        String getName();
        String getCode();
    }
}
