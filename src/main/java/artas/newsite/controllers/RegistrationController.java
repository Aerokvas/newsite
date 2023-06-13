package artas.newsite.controllers;

import artas.newsite.entities.PersonEntity;
import artas.newsite.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final PersonService personService;

    public RegistrationController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/registration")
    public String showRegistration(Model model) {
        model.addAttribute("userForm", new PersonEntity());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid PersonEntity userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!userForm.getPassword().equals(userForm.getConfirmPassword())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }

        if (!personService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/login";
    }
}
