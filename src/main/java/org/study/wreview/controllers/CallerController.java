package org.study.wreview.controllers;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.Caller;
import org.study.wreview.services.CallerService;

import java.util.Optional;

@Controller
@RequestMapping("/caller")
public class CallerController {
    final
    CallerService callerService;
    final
    PasswordEncoder passwordEncoder;

    public CallerController(CallerService callerService, PasswordEncoder passwordEncoder) {
        this.callerService = callerService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("callers", callerService.findAll());
        return "caller/index";
    }

    @GetMapping("/add")
    public String addGet(@ModelAttribute("caller") Caller caller){
        return "caller/add";
    }

    @PostMapping("")
    public String addPost(@ModelAttribute("caller") @Valid Caller caller, BindingResult bindingResult) {
        callerService.findByUsername(caller.getUsername()).
                ifPresent(c -> bindingResult.rejectValue("username", "",
                        "Человек с таким именем уже существует"));
        if (bindingResult.hasErrors())
            return "/caller/add";
        caller.setPassword(passwordEncoder.encode(caller.getPassword()));
        caller.setEnabled(true);
        caller.setRole("ROLE_USER");
        callerService.save(caller);

        return "redirect:/caller";
    }

    @GetMapping("/{name}/edit")
    public String editGet(Model model, @PathVariable("name") String name) {
        Optional<Caller> caller = callerService.findByUsername(name);
        if(caller.isPresent()){
            model.addAttribute("caller", caller.get());
            return "caller/edit";
        } else {
            return "redirect:/caller";
        }
    }
    @PatchMapping("/{name}")
    public String editPatch(@ModelAttribute("caller") @Valid Caller caller, BindingResult bindingResult,
                         @PathVariable("name") String name) {

        callerService.findByUsername(name).ifPresent(c -> {
            if(c.getRole().equals("ROLE_ADMIN")){
                bindingResult.rejectValue("username", "",
                        "Администратора нельзя редактировать");
            }
        });

        if (bindingResult.hasErrors())
            return "caller/edit";

        callerService.update(name, caller);
        return "redirect:/caller";
    }
}

