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
    private String role;
    private Boolean active;
    private Date createdTs;

    private boolean flag;

    public UserVO(User user) {
        this(user, 1L);
    }

    public UserVO(User user, long rowId) {
        this.rowId = rowId;
        this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.language = user.getLanguage();
        this.mobileNo = user.getMobileNo();
        this.role = user.getRole().getRoleName().name();
        this.active = user.isActive();
        this.createdTs = user.getCreatedTs();
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getUsername() {
        return this.username.toLowerCase();
    }
}