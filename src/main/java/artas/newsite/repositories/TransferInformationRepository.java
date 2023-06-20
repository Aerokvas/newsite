package artas.newsite.repositories;

import artas.newsite.entities.TransferInformationEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferInformationRepository extends JpaRepository<TransferInformationEntity, Integer> {
    List<TransferInformationEntity> findByFromAccountNumberOrToAccountNumber(String fromAccount, String toAccount);
}
