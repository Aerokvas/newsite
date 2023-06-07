package artas.newsite.service;

import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.PersonRoleEntity;
import artas.newsite.entities.RoleEntity;
import artas.newsite.repositories.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Service("customPersonDetails")
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        /*try {*/
            Optional<PersonEntity> person = personRepository.findByUsername(login);

            if (person.isEmpty()) {
                throw new UsernameNotFoundException("Такого пользователя нет емае");
            }

            Set<RoleEntity> roles = person.get().getPersonRoles().stream()
                    .map(PersonRoleEntity::getRole)
                    .collect(Collectors.toSet());
            out.println(person.get().getUsername());
            out.println(person.get().getPassword());
            out.println(roles.toString());
            return new User(person.get().getUsername(), person.get().getPassword(), getAuthorities(roles));
       /* } catch (Exception e) {
            throw new UsernameNotFoundException("опять что-то не так " + e.getMessage());
        }*/
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}