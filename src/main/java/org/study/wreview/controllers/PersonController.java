package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;
import org.study.wreview.services.PersonWithRatingService;

import java.util.Optional;

@Controller
@RequestMapping("person")
@RequiredArgsConstructor
public class PersonController {
    final private PersonService personService;
    final private PersonWithRatingService personWithRatingService;
    @GetMapping("")
    public String index(Model model){
        model.addAttribute("persons", personWithRatingService.findAll());
        return "person/index";
    }

    @GetMapping("/{name}")
    public String editGet(@PathVariable("name") String name, Model model){
        Optional<Person> personOptional = personService.findByUsername(name);
        if(personOptional.isPresent()){
            model.addAttribute("person", personOptional.get());
            return "person/edit";
        }
        return "redirect:/person";
    }
    @PatchMapping("/{name}")
    public String editPatch(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult, @PathVariable("name") String name){
        if(bindingResult.hasErrors()){
            return "person/edit";
        }
        personService.update(name, person);
        return "redirect:/person";
    }

}
