package com.example.jingangfarmmanagement.repository.entity;
import lombok.*;
import org.apache.logging.log4j.message.Message;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User extends BaseEntity {
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "full_name")
    private String fullName;

    @Transient
    private List<Role> role;

//    @ManyToOne()
//    @NotNull()
//    @JoinColumn(name = "role")
//    private Role role;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "cage_id")
    )
    private List<Cage> cages = new ArrayList<>();
}
