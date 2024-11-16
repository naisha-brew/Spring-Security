package com.gsmarin.springsecurity.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_roles")
@Setter
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Getter
    private String name;

    public enum Values{
        ADMIN(1L),
        BASIC(2L);


        long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }
    }

}
