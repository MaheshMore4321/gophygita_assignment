package sd.sym.assignment.gophygita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "UserMaster")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    private long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "mobileNo", nullable = true)
    private long mobileNo;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "createdTs", insertable = false, nullable = false, columnDefinition = " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ")
    private Date createdTs;

    @Column(name = "updatedTs", insertable = false, nullable = false, columnDefinition = " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ")
    private Date updatedTs;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "UserRoleMapMaster", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles;

    public User(String name, String username, String password, String language, long mobileNo) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.language = language;
        this.mobileNo = mobileNo;
    }
}