package com.gsmarin.springsecurity.service;

import com.gsmarin.springsecurity.Entity.Role;
import com.gsmarin.springsecurity.Repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(String name) {
         return roleRepository.findByName(name);
    }
}
