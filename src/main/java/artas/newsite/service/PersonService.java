package artas.newsite.service;

import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.PersonRoleEntity;
import artas.newsite.entities.RoleEntity;
import artas.newsite.repositories.PersonRepository;
import artas.newsite.repositories.RoleRepository;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PersonService(PersonRepository personRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PersonEntity findUserById(int userId) {
        Optional<PersonEntity> userFromDb = personRepository.findById(userId);
        PersonEntity user = userFromDb.orElse(new PersonEntity());
        Hibernate.initialize(user.getPersonRoles());
        return user;
    }

    public int findMaxAccountCountById(Integer id) {
        return personRepository.findMaxAccountCountById(id);
    }

    public List<PersonEntity> allUsers() {
        return personRepository.findAll();
    }

    public boolean saveUser(PersonEntity user) {
        try{
            Optional<PersonEntity> userFromDB = personRepository.findByUsername(user.getUsername());

            if (userFromDB.isPresent()) {
                return false;
            }

            RoleEntity role = roleRepository.findByName("ROLE_USER");

            PersonRoleEntity userRole = new PersonRoleEntity();
            userRole.setPerson(user);
            userRole.setRole(role);
            user.getPersonRoles().add(userRole);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setMaxAccountCount(0);

            out.println(user.toString());
            out.println(userRole.toString());

            personRepository.save(user);

            return true;
        } catch (Exception e){
            out.println(e.getMessage());
        }

        return false;
    }

    public boolean deleteUser(int userId) {
        if (personRepository.findById(userId).isPresent()) {
            personRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
