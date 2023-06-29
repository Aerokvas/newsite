package artas.newsite.entities;

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

    @Positive(message = "Сумма перевода должна быть больше 0")
    @Column(name = "amount")
    private BigDecimal amount;

    @NotBlank
    @Column(name = "fromaccount")
    private String fromAccountNumber;

    @NotBlank(message = "Неверный номер получателя")
    @Size(min = 8, max = 10, message = "Счет получателя должен состоять из 10 цифр")
    @Column(name = "toaccount")
    private String toAccountNumber;

    public TransferInformationEntity(@NotNull String fromAccountNumber, @NotNull String toAccountNumber, @NotNull BigDecimal amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }

    public String toString(){
        return "[" + getClass().getSimpleName() + "]" + ": id - " + getId()
                + "; fromAccount - " + getFromAccountNumber()
                + "; toAccount - " + getToAccountNumber()
                + "; amount - " + getAmount();
    }
}
