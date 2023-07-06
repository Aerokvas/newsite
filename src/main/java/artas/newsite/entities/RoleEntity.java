package artas.newsite.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "ROLE", schema = "PUBLIC", catalog = "BANKSDB")
public class RoleEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Basic
    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<PersonRoleEntity> personRoles = new HashSet<>();
}
