package org.study.wreview.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.study.wreview.models.Person;
import org.study.wreview.services.PersonService;
import org.study.wreview.utils.Sorting;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainController {

    PersonService personService;

    @GetMapping("")
    String index(Model model){
        List<Person> persons = personService.findWorkersWithRating();
        model.addAttribute("persons", persons);
        return "index";
    }

    @GetMapping("/error")
    String error(){
        return "myerror";
    }
}
