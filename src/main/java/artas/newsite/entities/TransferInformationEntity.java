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

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "fromaccount")
    private String fromAccountNumber;

    @Column(name = "toaccount")
    private String toAccountNumber;

    public String toString(){
        return "[" + getClass().getSimpleName() + "]" + ": id - " + getId()
                + "; fromAccount - " + getFromAccountNumber()
                + "; toAccount - " + getToAccountNumber()
                + "; amount - " + getAmount();
    }
}
