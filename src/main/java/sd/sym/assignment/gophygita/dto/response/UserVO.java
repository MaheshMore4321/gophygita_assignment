package sd.sym.assignment.gophygita.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private long rowId;
    private long userId;
    private String name;
    private String username;
    private String language;
    private long mobileNo;
    private List<String> roles;
    private Boolean active;
    private Date createdTs;

    private boolean flag;

    public UserVO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.language = user.getLanguage();
        this.mobileNo = user.getMobileNo();
        Set<Role> role = user.getRoles();
        this.roles = role.stream()
                .map(item -> { return item.getRoleName().name(); })
                .collect(Collectors.toList());
        this.active = user.isActive();
        this.createdTs = user.getCreatedTs();
    }

    public UserVO(User user, long rowId) {
        this.rowId = rowId;
        this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.language = user.getLanguage();
        this.mobileNo = user.getMobileNo();
        Set<Role> role = user.getRoles();
        this.roles = role.stream()
                .map(item -> { return item.getRoleName().name(); })
                .collect(Collectors.toList());
        this.active = user.isActive();
        this.createdTs = user.getCreatedTs();
    }
}