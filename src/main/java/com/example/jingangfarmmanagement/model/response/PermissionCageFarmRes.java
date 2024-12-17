package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionCageFarmRes {
    private List<Long> userIds;
    private Cage cage;

}
