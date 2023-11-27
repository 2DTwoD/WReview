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
import org.study.wreview.utils.Sorting;

import java.util.Optional;

@Controller
@RequestMapping("/person")
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
        if(person.isEmpty()){
            return "redirect:/error";
        }

        model.addAttribute("person", person.get());
        return "person/info";
    }

    @DeleteMapping("/{name}")
    public String info(@PathVariable("name") String name){
        Optional<Person> person = personService.findWorkerByUsername(CurrentUserInfo.getUsername());
        if(person.isEmpty() || !person.get().getRole().equals("ROLE_ADMIN")){
            return "redirect:/error";
        }
        personService.findByUsername(name)
                .filter(p -> p.getRole().equals("ROLE_USER"))
                .ifPresent(personService::delete);
        return "redirect:/person";
    }

    @GetMapping("/edit")
    public String editGet(Model model){
        Optional<Person> person = personService.findByUsername(CurrentUserInfo.getUsername());
        if(person.isEmpty()) {
            return "redirect:/error";
        }
        model.addAttribute("person", person.get());
        return "person/edit";
    }

    @PatchMapping("/edit")
    public String editPatch(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult){
        if(person.currentUserNotMe()) {
            return "redirect:/error";
        }
        if(bindingResult.hasErrors()){
            return "person/edit";
        }
        personService.update(CurrentUserInfo.getUsername(), person);
        return "redirect:/person/edit";
    }

    @GetMapping("/edit_pass")
    public String editPassGet(@ModelAttribute("password") NewPassword password){
        Optional<Person> personForUpdate = personService.findByUsername(CurrentUserInfo.getUsername());
        if(personForUpdate.isEmpty()) {
            return "redirect:/error";
        }
        return "person/edit_pass";
    }

    @PatchMapping("/edit_pass")
    public String editPassPatch(@ModelAttribute("password") NewPassword password,
                                BindingResult bindingResult){
        Person personForUpdate = personService.findByUsername(CurrentUserInfo.getUsername()).orElse(null);
        if(personForUpdate == null) {
            return "redirect:/error";
        }
        if(!passwordEncoder.matches(password.oldPassword(), personForUpdate.getPassword())){
            bindingResult.rejectValue("oldPassword", "", "Неправильный пароль!");
        }
        if(!password.newPassword().equals(password.confirmPassword())){
            bindingResult.rejectValue("confirmPassword", "", "Подтверждение пароля отличается");
        }

        if(bindingResult.hasErrors()){
            return "person/edit_pass";
        }
        personForUpdate.setPassword(passwordEncoder.encode(password.newPassword()));
        personService.save(personForUpdate);
        return "redirect:/person/edit";
    }

}
