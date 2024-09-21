package com.example.jingangfarmmanagement.model.req;

import lombok.Data;

import java.util.List;

@Data
public class PermissionCageReq {
    private Long userId;
    private List<Long> cageIds;
}
