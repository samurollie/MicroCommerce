package com.microcommerice.customers.config;


import com.microcommerice.customers.entity.Role;
import com.microcommerice.customers.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createRoleIfNotFound(Role.ERole.ROLE_USER);
        createRoleIfNotFound(Role.ERole.ROLE_ADMIN);
        createRoleIfNotFound(Role.ERole.ROLE_SERVICE);

        alreadySetup = true;
    }

    private void createRoleIfNotFound(Role.ERole name) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}