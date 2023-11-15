package org.study.wreview.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    final private PersonService personService;
    @GetMapping("")
    String index(Model model){
        List<Person> persons = personService.findWorkers();
        persons.sort((p1, p2) -> (p2.getRating() - p1.getRating()));
        model.addAttribute("persons", persons);
        return "index";
    }

    @GetMapping("/test")
    String test(){
        return "test";
    }
}
