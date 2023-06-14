package artas.newsite.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    /*@NotNull
    @NotBlank(message = "Логин обязателен.")
    @Size(min=5, message = "Логин должен быть от 5 символов..")*/
    @Column(name = "login")
    private String username;

    /*@NotNull
    @NotBlank(message = "Пароль обязателен.")
    @Size(min=5, max = 30,message = "Пароль должен быть от 5 до 30 символов..")*/
    @Column(name = "password")
    private String password;

    /*@NotNull
    @Size(min=5, max = 30,message = "Повторный пароль должен быть от 5 до 30 символов.")
    @NotBlank(message = "Повторный пароль обязателен.")*/
    @Transient
    private String confirmPassword;


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
                + "; role - " + getPersonRoles();
    }

}
