package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {
    String userName;
     String fullName;
     int status;
     String address;
     List<String> roleId;
     String email;

}
