package artas.newsite.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode
@Entity
@Table(name = "person", schema = "public", catalog = "BanksDb")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login")
    @NotBlank(message = "Логин обязателен.")
    @Size(min=5, message = "Логин должен быть от 5 символов.")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Пароль обязателен.")
    @Size(min=5, message = "Пароль должен быть от 5 символов.")
    private String password;

    @Transient
    @NotBlank(message = "Повторный пароль обязателен.")
    private String confirmPassword;

    @Column(name = "max_account_count")
    private Integer maxAccountCount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonRoleEntity> personRoles = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "personId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BankAccountEntity> bankAccounts = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonEntity)) return false;
        PersonEntity that = (PersonEntity) o;
        return getId() == that.getId() &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword());
    }

    @Override
    public String toString(){
        return "[" + getClass().getSimpleName() + "]"
                + ": id - " + getId()
                + "; login - " + getUsername()
                + "; password - " + getPassword()
                + "; accounts - " + getMaxAccountCount()
                + "; role - " + getPersonRoles();
    }

}
