package sd.sym.assignment.gophygita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(ERole roleName);

	Boolean existsByRoleName(ERole roleName);
}
