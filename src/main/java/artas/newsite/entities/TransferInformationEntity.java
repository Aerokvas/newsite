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
@Table(name = "transfer_information", schema = "public", catalog = "BanksDb")
public class TransferInformationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private BankAccountEntity fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private BankAccountEntity toAccount;

    @Transient
    private String toAccountNumber;

    @Positive(message = "Сумма перевода должна быть больше 0")
    @Column(name = "amount")
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
