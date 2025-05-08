package com.microcommerice.customers.config;

import com.microcommerice.customers.entity.Customer;
import com.microcommerice.customers.entity.Role;
import com.microcommerice.customers.entity.Role.ERole;
import com.microcommerice.customers.repository.CustomerRepository;
import com.microcommerice.customers.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Cria as roles se não existirem
        createRoleIfNotFound(ERole.ROLE_USER);
        createRoleIfNotFound(ERole.ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_SERVICE);

        // Criar usuário comum se não existir
        if (!customerRepository.existsByUsername("user")) {
            Customer user = new Customer(
                    "user",
                    passwordEncoder.encode("user123"),
                    "user@email.com",
                    "Usuario",
                    "Comum",
                    "83999999999");
            user.setRoles(Set.of(roleRepository.findByName(ERole.ROLE_USER).get()));
            user.setActive(true);
            customerRepository.save(user);
        }

        // Criar admin se não existir
        if (!customerRepository.existsByUsername("admin")) {
            Customer admin = new Customer(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "admin@email.com",
                    "Administrador",
                    "Sistema",
                    "83988888888");
            admin.setRoles(Set.of(roleRepository.findByName(ERole.ROLE_ADMIN).get()));
            admin.setActive(true);
            customerRepository.save(admin);
        }

        // Criar service se não existir
        if (!customerRepository.existsByUsername("service")) {
            Customer service = new Customer(
                    "service",
                    passwordEncoder.encode("service123"),
                    "service@email.com",
                    "Service",
                    "Account",
                    "83977777777");
            service.setRoles(Set.of(roleRepository.findByName(ERole.ROLE_SERVICE).get()));
            service.setActive(true);
            customerRepository.save(service);
        }
    }

    private void createRoleIfNotFound(ERole roleType) {
        if (!roleRepository.existsByName(roleType)) {
            Role role = new Role();
            role.setName(roleType);
            roleRepository.save(role);
        }
    }
}
