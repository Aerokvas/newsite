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
@Table(name = "bank_account", schema = "public", catalog = "BanksDb")
public class BankAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Size(min = 8, max = 10)
    @Column(name = "name_number")
    private String nameNumber;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personId;

    public synchronized void takeValue(BigDecimal value) {
        amount = amount.subtract(value);
    }

    public synchronized void putValue(BigDecimal value) {
        amount = amount.add(value);
    }

    public String toString(){
        return "[" + getClass().getSimpleName() + "]"
                + ": id - " + getId()
                + "; nameNumber - " + getNameNumber()
                + "; personId - " + getPersonId().getId();
    }
}
