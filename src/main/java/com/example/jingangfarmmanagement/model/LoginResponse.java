package com.example.jingangfarmmanagement.model;

import com.example.jingangfarmmanagement.repository.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String userName;
    private String fullName;
    private String userId;
    private List<Role> role;
}
