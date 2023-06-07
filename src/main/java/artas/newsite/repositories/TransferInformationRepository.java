package artas.newsite.repositories;

import artas.newsite.entities.TransferInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferInformationRepository extends JpaRepository<TransferInformationEntity, Integer> {
}
