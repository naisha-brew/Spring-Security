package com.gsmarin.springsecurity.config;

import com.gsmarin.springsecurity.Entity.Role;
import com.gsmarin.springsecurity.Entity.User;
import com.gsmarin.springsecurity.service.RoleService;
import com.gsmarin.springsecurity.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
@AllArgsConstructor
public class AdminUserConfig implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleService.findByName(Role.Values.ADMIN.name());
        var userAdmin = userService.findByUsername("Admin");

        if(userAdmin.isPresent() ) {
            System.out.println("admin exist");
            }
        else{
            var user = new User();
            var aux = bCryptPasswordEncoder.encode("12345");
            System.out.println(bCryptPasswordEncoder.matches("12345", aux));
            user.setUsername("Admin");
            user.setPassword(aux);
            user.setRoles(Set.of(roleAdmin));
            userService.save(user);
        }
    }
}
