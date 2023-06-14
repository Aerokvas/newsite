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
import org.springframework.ui.Model;

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

    public List<PersonEntity> allUsers() {
        return personRepository.findAll();
    }

    public boolean saveUser(PersonEntity person, Model model) {
        try {
            Optional<PersonEntity> userFromDB = personRepository.findByUsername(person.getUsername());

            if (userFromDB.isPresent()) {
                model.addAttribute("usernameError", "Такой пользователь уже существует");
                return false;
            }

            RoleEntity role = roleRepository.findByName("ROLE_USER");

            PersonRoleEntity personRole = new PersonRoleEntity();
            personRole.setPerson(person);
            personRole.setRole(role);
            person.getPersonRoles().add(personRole);
            person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));

            logger.info("Создание пользователя: " + person + "; Принадлежность: " + personRole);

            personRepository.save(person);

            logger.info("Пользователь создан");

            return true;
        } catch (Exception e) {
            logger.info("При регистрации что-то пошло не так" + e.getMessage(), e);
        }

        return false;
    }

    public boolean editRole(Optional<PersonEntity> person, List<Integer> rolesIds) {
        if (person.isPresent()) {
            Set<PersonRoleEntity> personRoles = person.get().getPersonRoles();
            List<RoleEntity> selectedRoles = roleRepository.findRolesByIds(rolesIds);

            personRoles.removeIf(personRole -> !selectedRoles.contains(personRole.getRole()));

            logger.info("Выбранные роли " + selectedRoles);

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
