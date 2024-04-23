package com.example.jingangfarmmanagement.model.req;

import lombok.Data;

import java.util.List;

@Data
public class AssignUserRoleReq {

    private Long userId;
    private List<Long> roleIds;
}
