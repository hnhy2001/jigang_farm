package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.Role;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class UserRes {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String address;
    private String fullName;
    private int status;
    private List<Role> role;
}
