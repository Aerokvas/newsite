package artas.newsite.repositories;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {
    List<BankAccountEntity> getBankAccountEntitiesByPersonId(PersonEntity person);
    BankAccountEntity getBankAccountEntityByNameNumber(String nameNumber);
}
