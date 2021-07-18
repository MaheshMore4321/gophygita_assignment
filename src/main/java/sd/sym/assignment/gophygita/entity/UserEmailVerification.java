package sd.sym.assignment.gophygita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "UserEmailVerificationMaster")
public class UserEmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rowId")
    private long rowId;

    @Column(name = "userId", nullable = false)
    private long userId;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "verificationCode", nullable = false)
    private String verificationCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdTs", insertable = false, nullable = false, columnDefinition = " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ")
    private Date createdTs;

    @Column(name = "verificationStatus", nullable = false)
    private boolean verificationStatus;
}