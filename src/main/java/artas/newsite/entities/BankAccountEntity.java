package artas.newsite.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

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
    @Basic
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic
    @Column(name = "person_id")
    private Integer userId;
    @Basic
    @Column(name = "name_number")
    private String nameNumber;

    public synchronized void takeValue(BigDecimal value) {
        amount = amount.subtract(value);
    }

    public synchronized void putValue(BigDecimal value) {
        amount = amount.add(value);
    }
}
