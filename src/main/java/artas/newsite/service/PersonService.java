package artas.newsite.service;

import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.PersonRoleEntity;
import artas.newsite.entities.RoleEntity;
import artas.newsite.repositories.PersonRepository;
import artas.newsite.repositories.RoleRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.out;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Log logger = LogFactory.getLog(getClass());

    public PersonService(PersonRepository personRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public int findMaxAccountCountById(Integer id) {
        return personRepository.findMaxAccountCountById(id);
    }

    public List<PersonEntity> allUsers() {
        return personRepository.findAll();
    }

    public boolean saveUser(PersonEntity user) {
        try {
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

            logger.info("Пользователь создан: " + user.toString());
            logger.info("Принадлежность: " + userRole.toString());

            personRepository.save(user);

            return true;
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return false;
    }

    public boolean editRole(Optional<PersonEntity> person, List<Integer> rolesIds) {
        if (person.isPresent()) {
            Set<PersonRoleEntity> personRoles = person.get().getPersonRoles();
            List<RoleEntity> selectedRoles = roleRepository.findRolesByIds(rolesIds);

            // Удаление ролей, которые не выбраны
            personRoles.removeIf(personRole -> !selectedRoles.contains(personRole.getRole()));
            logger.info("Выбранные роли " + selectedRoles);

            // Добавление новых ролей
            for (RoleEntity role : selectedRoles) {
                if (personRoles.stream().noneMatch(personRole -> personRole.getRole().equals(role))) {
                    PersonRoleEntity personRole = new PersonRoleEntity();
                    personRole.setPerson(person.get());
                    personRole.setRole(role);
                    personRoles.add(personRole);
                    logger.info("Добавлена роль " + role);
                }
            }
            personRepository.save(person.get());
            return true;
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
