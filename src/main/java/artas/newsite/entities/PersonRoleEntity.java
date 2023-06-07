package artas.newsite.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
//@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person_role", schema = "public", catalog = "BanksDb")
public class PersonRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private PersonEntity person;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonRoleEntity)) return false;
        PersonRoleEntity that = (PersonRoleEntity) o;
        return getId() == that.getId() &&
                Objects.equals(getRole(), that.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRole());
    }

    @Override
    public String toString(){
        return getId() + " " + person.getId() + " " + role.getId();
    }
}
