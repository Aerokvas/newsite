package artas.newsite.repositories;

import artas.newsite.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName (String name);
    @Query("SELECT r FROM RoleEntity r WHERE r.id IN :rolesId")
    List<RoleEntity> findRolesByIds(@Param("rolesId") List<Integer> rolesId);

}
