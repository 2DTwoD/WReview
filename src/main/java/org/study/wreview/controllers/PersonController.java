package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.NewPassword;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;
import org.study.wreview.utils.CurrentUserInfo;
import org.study.wreview.utils.PaginationFilterEngine;

import java.util.Optional;

@Controller
@RequestMapping("/persons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonController implements PaginationFilterEngine {

    PersonService personService;
    PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String index(Model model,
                        @RequestParam(name = "pageNum", required = false) Integer pageNum,
                        @RequestParam(name = "filter", required = false) String filter) {

        model.addAttribute("persons", getPage(model, pageNum, filter,
                Sort.by("username"),
                (pageable, f) -> personService.findAllWithFilter(f, pageable)
        ));

        return "person/index";
    }
    @GetMapping("/workers")
    public String workers(Model model,
                        @RequestParam(name = "pageNum", required = false) Integer pageNum,
                        @RequestParam(name = "filter", required = false) String filter) {

        model.addAttribute("persons", getPage(model, pageNum, filter,
                Sort.by("username"),
                (pageable, f) -> personService.findWorkersWithFilter(f, pageable)
        ));

        return "person/index";
    }

    @GetMapping("/{name}")
    public String info(@PathVariable("name") String name, Model model){

        Optional<Person> person = personService.findByUsername(name);
        if(person.isEmpty()) {
            return "error";
        }

        model.addAttribute("person", person.get());
        return "person/info";
    }

    @DeleteMapping("/{name}")
    public String delete(@PathVariable("name") String name){
        Optional<Person> person = personService.findByUsername(CurrentUserInfo.getUsername());
        if(person.isEmpty() || !person.get().isAdmin()){
            return "error";
        }
        personService.findByUsername(name)
                .filter(Person::isUser)
                .ifPresent(personService::delete);
        return "redirect:/persons";
    }

    @PatchMapping("/{name}/lock")
    public String block(@PathVariable("name") String name){
        Optional<Person> person = personService.findByUsername(name);
        if(person.isEmpty() || person.get().isAdmin()){
            return "error";
        }
        personService.findByUsername(name)
                .filter(Person::isUser)
                .ifPresent(p -> personService.block(name));
        return "redirect:/persons";
    }

    @GetMapping("/edit")
    public String editGet(Model model){
        Optional<Person> person = personService.findByUsername(CurrentUserInfo.getUsername());
        if(person.isEmpty()) {
            return "error";
        }
        model.addAttribute("person", person.get());
        return "person/edit";
    }

    @PatchMapping("/edit")
    public String editPatch(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult){
        if(person.currentUserNotMe()) {
            return "error";
        }
        if(bindingResult.hasErrors()){
            return "person/edit";
        }
        personService.update(CurrentUserInfo.getUsername(), person);
        return "redirect:/persons/edit";
    }

    @GetMapping("/edit_pass")
    public String editPassGet(@ModelAttribute("password") NewPassword password){
        Optional<Person> personForUpdate = personService.findByUsername(CurrentUserInfo.getUsername());
        if(personForUpdate.isEmpty()) {
            return "error";
        }
        return "person/edit_pass";
    }

    @PatchMapping("/edit_pass")
    public String editPassPatch(@ModelAttribute("password") NewPassword password,
                                BindingResult bindingResult){
        Optional<Person> personForUpdate = personService.findByUsername(CurrentUserInfo.getUsername());
        if(personForUpdate.isEmpty()) {
            return "error";
        }
        if(!passwordEncoder.matches(password.oldPassword(), personForUpdate.get().getPassword())){
            bindingResult.rejectValue("oldPassword", "", "Неправильный пароль!");
        }
        if(!password.newPassword().equals(password.confirmPassword())){
            bindingResult.rejectValue("confirmPassword", "", "Подтверждение пароля отличается");
        }

        if(bindingResult.hasErrors()){
            return "person/edit_pass";
        }
        personForUpdate.get().setPassword(passwordEncoder.encode(password.newPassword()));
        personService.save(personForUpdate.get());
        return "redirect:/persons/edit";
    }

}
