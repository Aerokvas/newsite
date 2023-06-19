package artas.newsite.repositories;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {
    List<BankAccountEntity> getBankAccountEntitiesByPersonId(PersonEntity person);
    BankAccountEntity getBankAccountEntityByNameNumber(String nameNumber);
    @Query("SELECT COUNT(b) FROM BankAccountEntity b WHERE b.personId.id = :personId")
    int getAccountsCountByPersonId(@Param("personId") Integer personId);
}
