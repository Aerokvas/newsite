package artas.newsite.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "transfer_information", schema = "public", catalog = "BanksDb")
public class TransferInformationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "amount")
    private BigDecimal amount;
    @Transient
    private String fromAccountNumber;
    @ManyToOne
    @JoinColumn(name = "fromaccount", referencedColumnName = "id")
    private BankAccountEntity bankAccountByFromaccount;
    @Transient
    private String toAccountNumber;
    @ManyToOne
    @JoinColumn(name = "toaccount", referencedColumnName = "id")
    private BankAccountEntity bankAccountByToaccount;
}
