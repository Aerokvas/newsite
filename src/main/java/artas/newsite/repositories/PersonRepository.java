package artas.newsite.repositories;

import artas.newsite.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
    Optional<PersonEntity> findByUsername(@Param("login") String login);

    @Query("SELECT p.maxAccountCount FROM PersonEntity p WHERE p.id = :id")
    Integer findMaxAccountCountById(Integer id);
}
