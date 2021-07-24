package sd.sym.assignment.gophygita.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.dao.GoPhygitalDao;
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
    private GoPhygitalDao goPhygitalDao;

    @Autowired
    private UserEmailVerificationRepository userEmailVerificationRepository;

    public UserVO getUserData(long userId) {
        return new UserVO(goPhygitalDao.getUserDataForUserId(userId));
    }

    public List<UserVO> getAdminData() {
        List<User> userList = goPhygitalDao.getAllUserData();

        int index = 1;
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            Role role = user.getRole();
            if(!role.getRoleName().equals(ERole.ROLE_ADMIN)){
                userVOList.add(new UserVO(user, index++));
            }
        }
        return userVOList;
    }

    public MessageResponse toggleUserActive(long userId) {
        User user = goPhygitalDao.getUserDataForUserId(userId);
        if(null != user) {
            user.setActive(!user.isActive());
            user.setUpdatedTs(Utility.getNowDate());
            goPhygitalDao.saveUser(user);
            return new MessageResponse(true, "User toggleUserActive Successfully");
        }
        return new MessageResponse(false, "User toggleUserActive Failed || UserId not found");
    }
}