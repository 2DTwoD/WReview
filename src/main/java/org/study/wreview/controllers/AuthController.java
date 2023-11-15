package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    final private PersonService personService;
    final private PasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String registrationPost(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "auth/registration";
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setEnabled(true);
        System.out.println(person);
        personService.save(person);
        return "redirect:/";
    }

}
