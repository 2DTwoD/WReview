package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.NewPassword;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;
import org.study.wreview.utils.CurrentUserInfo;
import org.study.wreview.utils.Sorting;

import java.util.Optional;

@Controller
@RequestMapping("/person")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonController {
    PersonService personService;
    CurrentUserInfo currentUserInfo;
    PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String index(Model model){
        model.addAttribute("persons", personService.findWorkers(Sorting.NAME_ASC));
        return "person/index";
    }

    @GetMapping("/{name}")
    public String info(@PathVariable("name") String name, Model model){
        Optional<Person> personOptional = personService.findByUsername(name);
        if(personOptional.isPresent()){
            model.addAttribute("person", personOptional.get());
            return "person/info";
        }
        return "redirect:/person";
    }

    @DeleteMapping("/{name}")
    public String info(@PathVariable("name") String name){
        personService.findByUsername(name).ifPresent(personService::delete);
        return "redirect:/person";
    }

    @GetMapping("/edit")
    public String editGet(Model model){
        Optional<Person> person = personService.findByUsername(currentUserInfo.getUsername());
        if(person.isEmpty()) {
            return "redirect:/error";
        }
        model.addAttribute("person", person.get());
        return "person/edit";
    }

    @PatchMapping("/edit")
    public String editPatch(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "person/edit";
        }
        Optional<Person> personForUpdate = personService.findByUsername(currentUserInfo.getUsername());
        if(personForUpdate.isEmpty()) {
            return "redirect:/error";
        }
        personService.update(currentUserInfo.getUsername(), personForUpdate.get());
        return "redirect:/";
    }

    @GetMapping("/edit_pass")
    public String editPassGet(@ModelAttribute("password") NewPassword password){
        Optional<Person> personForUpdate = personService.findByUsername(currentUserInfo.getUsername());
        if(personForUpdate.isEmpty()) {
            return "redirect:/error";
        }
        return "person/edit_pass";
    }

    @PatchMapping("/edit_pass")
    public String editPassPatch(@ModelAttribute("password") NewPassword password,
                                BindingResult bindingResult){
        Person personForUpdate = personService.findByUsername(currentUserInfo.getUsername()).orElse(null);
        if(personForUpdate == null) {
            return "redirect:/error";
        }
        if(!passwordEncoder.matches(password.oldPassword(), personForUpdate.getPassword())){
            bindingResult.rejectValue("oldPassword", "", "Неправильный пароль!");
        }
        if(!password.newPassword().equals(password.confirmPassword())){
            bindingResult.rejectValue("confirmPassword", "", "Подтверждение пароля отличается");
        }
        System.out.println(password.oldPassword());
        if(bindingResult.hasErrors()){
            return "person/edit_pass";
        }
        personForUpdate.setPassword(passwordEncoder.encode(password.newPassword()));
        personService.save(personForUpdate);
        return "redirect:/person/edit";
    }

}
