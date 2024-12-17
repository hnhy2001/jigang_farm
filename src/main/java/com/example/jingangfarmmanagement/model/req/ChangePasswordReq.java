package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordReq {
    private String userId;
    private String newPassword;
}
