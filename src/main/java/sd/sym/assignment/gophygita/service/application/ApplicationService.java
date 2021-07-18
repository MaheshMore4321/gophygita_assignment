package sd.sym.assignment.gophygita.service.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.dto.response.MessageResponse;
import sd.sym.assignment.gophygita.dto.response.UserVO;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;
import sd.sym.assignment.gophygita.repository.UserEmailVerificationRepository;
import sd.sym.assignment.gophygita.repository.UserRepository;
import sd.sym.assignment.gophygita.utility.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEmailVerificationRepository userEmailVerificationRepository;

    public UserVO getUserData(long userId) {
        UserVO userVO = new UserVO();
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            userVO = new UserVO(user);
        }
        return userVO;
    }

    public List<UserVO> getAdminData() {
        List<User> userList = userRepository.findAll();

        int index = 1;
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            boolean flag = true;
            Set<Role> roles = user.getRoles();
            for(Role role : roles) {
                if(role.getRoleName().equals(ERole.ROLE_ADMIN)){
                    flag = false;
                    break;
                }
            }

            if(flag) {
                userVOList.add(new UserVO(user, index++));
            }
        }
        return userVOList;
    }

    public MessageResponse toggleUserActive(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(!user.isActive());
            user.setUpdatedTs(Utility.getNowDate());
            userRepository.save(user);
            return new MessageResponse(true, "User toggleUserActive Successfully");
        }
        return new MessageResponse(false, "User toggleUserActive Failed || UserId not found");
    }
}