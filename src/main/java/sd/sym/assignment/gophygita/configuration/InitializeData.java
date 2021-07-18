package sd.sym.assignment.gophygita.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;
import sd.sym.assignment.gophygita.repository.RoleRepository;
import sd.sym.assignment.gophygita.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class InitializeData {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {

        if(!roleRepository.existsByRoleName(ERole.ROLE_USER)){
            roleRepository.save(new Role(ERole.ROLE_USER));
        }
        if(!roleRepository.existsByRoleName(ERole.ROLE_ADMIN)){
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        String adminUsername = "maheshmore4321@gmail.com";
        if(!userRepository.existsByUsername(adminUsername)){

            User user = new User();
            user.setName("Mahesh More");
            user.setUsername(adminUsername);
            user.setPassword(encoder.encode("987654321"));
            user.setLanguage("EN");
            user.setMobileNo(9876543210L);
            user.setActive(true);

            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            user.setRoles(roles);

            userRepository.save(user);
        }
    }
}