package sd.sym.assignment.gophygita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sd.sym.assignment.gophygita.entity.UserEmailVerification;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEmailVerificationRepository extends JpaRepository<UserEmailVerification, Long> {

    Optional<List<UserEmailVerification>> findByUserIdAndPurposeOrderByRowIdDesc(long userId, String purpose);
}
