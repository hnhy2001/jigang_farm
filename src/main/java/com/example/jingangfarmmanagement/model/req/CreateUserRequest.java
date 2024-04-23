package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String code;
    private String userName;
    private String password;
    private String email;
    private String address;
    private Long unitId;
}
