package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;
import org.study.wreview.utils.Sorting;

import java.util.Optional;

@Controller
@RequestMapping("/person")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonController {
    PersonService personService;
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
