package com.example.jingangfarmmanagement.model.req;

import lombok.Data;

import java.util.List;

@Data
public class AssignMenuPermissionReq {

    private Long menuId;
    private List<Long> permissionIds;
}
