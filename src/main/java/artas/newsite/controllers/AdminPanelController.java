package artas.newsite.controllers;

import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.PersonRoleEntity;
import artas.newsite.entities.RoleEntity;
import artas.newsite.repositories.PersonRepository;
import artas.newsite.repositories.RoleRepository;
import artas.newsite.service.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class AdminPanelController {
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final RoleRepository roleRepository;
    private final Log logger = LogFactory.getLog(getClass());

    public AdminPanelController(PersonRepository personRepository, PersonService personService, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin")
    public String showUserList(Model model) {
        logger.info("Админка запущена");
        List<PersonEntity> users = personRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        try {
            if (personService.deleteUser(id)) {
                List<PersonEntity> users = personRepository.findAll();
                model.addAttribute("users", users);
                return "redirect:/admin ";
            } else {
                List<PersonEntity> users = personRepository.findAll();
                model.addAttribute("users", users);
                model.addAttribute("error", "Произошла ошибка при удалении");
                return "redirect:/admin";
            }
        } catch (Exception e) {
            logger.info("Ошибка в удалении - " + e.getMessage(), e);
        }

        return "redirect:/admin";
    }

    @GetMapping("admin/edit/{id}")
    public String showEditUserRoleForm(@PathVariable Integer id, Model model) {
        logger.info("Редактирование запущено");
        Optional<PersonEntity> person = personRepository.findById(id);

        if (person.isPresent()) {
            Set<PersonRoleEntity> userRoles = person.get().getPersonRoles();
            List<RoleEntity> availableRoles = roleRepository.findAll();

            model.addAttribute("person", person.get());
            model.addAttribute("userRoles", userRoles);
            model.addAttribute("availableRoles", availableRoles);
        }

        return "admin_user_edit";
    }

    @PostMapping("admin/edit/{id}")
    public String editUserRole(@PathVariable Integer id, @RequestParam(required = false, name = "personRoles") List<Integer> rolesIds, Model model) {
        if (rolesIds != null && !rolesIds.isEmpty()) {
            Optional<PersonEntity> person = personRepository.findById(id);

            logger.info("Входные роли " + rolesIds);

            if (personService.editRole(person, rolesIds)) {
                return "redirect:/admin";
            } else {
                model.addAttribute("errorEditing", "При добавлении ролей что-то пошло не так.");
                return "redirect:/admin/edit/" + id;
            }
        } else {
            model.addAttribute("errorEditing", "Вы ничего не выбрали.");
            return "redirect:/admin/edit/" + id;
        }
    }

}
