package com.example.jingangfarmmanagement.model.req;

import lombok.Data;

import java.util.List;

@Data
public class AssignRoleMenuReq {
    private Long roleId;
    private List<Long> menuIds;
}
