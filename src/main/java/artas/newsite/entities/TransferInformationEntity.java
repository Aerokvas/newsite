package artas.newsite.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "TRANSFER_INFORMATION", schema = "PUBLIC", catalog = "BANKSDB")
public class TransferInformationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "FROM_ACCOUNT_ID")
    private BankAccountEntity fromAccount;

    @ManyToOne
    @JoinColumn(name = "TO_ACCOUNT_ID")
    private BankAccountEntity toAccount;

    @Transient
    private String toAccountNumber;

    @Positive(message = "Сумма перевода должна быть больше 0")
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    public TransferInformationEntity(BankAccountEntity fromAccount, BankAccountEntity toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String toString() {
        return "[" + getClass().getSimpleName() + "]" + ": id - " + getId()
                + "; fromAccount - " + getFromAccount().getNameNumber()
                + "; toAccount - " + getToAccountNumber()
                + "; amount - " + getAmount();
    }
}
