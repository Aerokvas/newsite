package artas.newsite.repositories;

import artas.newsite.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {
    List<BankAccountEntity> getBankAccountEntitiesByUserId(int userId);
    BankAccountEntity getBankAccountEntityByNameNumber(String nameNumber);
}
