package artas.newsite.repositories;

import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
    Optional<PersonEntity> findByUsername(@Param("login") String login);

    @Query("SELECT r.role FROM PersonEntity p JOIN p.personRoles r WHERE p.id = :id")
    List<RoleEntity> findUserRoles(@Param("id") Integer id);
}
