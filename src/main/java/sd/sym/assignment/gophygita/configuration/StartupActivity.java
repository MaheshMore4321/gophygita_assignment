package sd.sym.assignment.gophygita.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.dao.GoPhygitalDao;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;

@Configuration
public class StartupActivity {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private GoPhygitalDao goPhygitalDao;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {

        if(!goPhygitalDao.existsByRoleName(ERole.ROLE_USER)){
            goPhygitalDao.saveRole(new Role(ERole.ROLE_USER));
        }
        if(!goPhygitalDao.existsByRoleName(ERole.ROLE_ADMIN)){
            goPhygitalDao.saveRole(new Role(ERole.ROLE_ADMIN));
        }

        String adminUsername = "maheshmore4321@gmail.com";
        if(!goPhygitalDao.existsByUsername(adminUsername)){
            User user = new User();
            user.setName("Mahesh More");
            user.setUsername(adminUsername);
            user.setPassword(encoder.encode("987654321"));
            user.setLanguage("EN");
            user.setMobileNo(9876543210L);
            user.setActive(true);
            user.setRole(goPhygitalDao.getRole(ERole.ROLE_ADMIN));
            goPhygitalDao.saveUser(user);
        }
    }
}