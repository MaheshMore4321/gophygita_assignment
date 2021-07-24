package sd.sym.assignment.gophygita.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;
import sd.sym.assignment.gophygita.entity.UserEmailVerification;
import sd.sym.assignment.gophygita.repository.RoleRepository;
import sd.sym.assignment.gophygita.repository.UserEmailVerificationRepository;
import sd.sym.assignment.gophygita.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class GoPhygitalDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserEmailVerificationRepository userEmailVerificationRepository;

    public Role getRole(ERole eRole){
        Role role = roleRepository.findByRoleName(eRole)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        return role;
    }

    public boolean existsByRoleName(ERole role) {
        return roleRepository.existsByRoleName(role);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserDataForUserId(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public List<User> getAllUserData() {
        return userRepository.findAll();
    }

    public User getUserForUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
           return userOptional.get();
        }
        return null;
    }

    public UserEmailVerification saveUserEmailVerification (UserEmailVerification userEmailVerification) {
        return userEmailVerificationRepository.save(userEmailVerification);
    }

    public List<UserEmailVerification> getUserEmailVerificationListOrderByRowIdDesc(long userId, String purpose) {
        Optional<List<UserEmailVerification>> optionalUserEmailVerifications =
                userEmailVerificationRepository.findByUserIdAndPurposeOrderByRowIdDesc(userId, purpose);

        if(optionalUserEmailVerifications.isPresent()) {
            return optionalUserEmailVerifications.get();
        }
        return null;
    }
}
