package artas.newsite.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "BANK_ACCOUNT", schema = "PUBLIC", catalog = "BANKSDB")
public class BankAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;

    @Size(min = 8, max = 10)
    @Column(name = "NAME_NUMBER")
    private String nameNumber;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private PersonEntity personId;

    public synchronized void takeValue(BigDecimal value) {
        amount = amount.subtract(value);
    }

    public synchronized void putValue(BigDecimal value) {
        amount = amount.add(value);
    }

    @Override
    public String toString(){
        return "[" + getClass().getSimpleName() + "]"
                + ": id - " + getId()
                + "; nameNumber - " + getNameNumber()
                + "; amount - " + getAmount()
                + "; personId - " + getPersonId().getId();
    }
}
