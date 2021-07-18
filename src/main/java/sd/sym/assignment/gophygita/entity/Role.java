package sd.sym.assignment.gophygita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sd.sym.assignment.gophygita.constant.ERole;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roleId")
    private long roleId;

    @NotBlank
    @Size(max = 10)
    @Enumerated(EnumType.STRING)
    @Column(name = "roleName", nullable = false)
    private ERole roleName;

    public Role(ERole roleName){
        this.roleName = roleName;
    }
}