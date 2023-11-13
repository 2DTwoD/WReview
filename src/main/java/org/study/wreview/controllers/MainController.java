package org.study.wreview.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.study.wreview.services.CallerService;

@Controller
public class MainController {
    final CallerService callerService;

    public MainController(CallerService callerService) {
        this.callerService = callerService;
    }

    @GetMapping("")
    String index(){
        return "index";
    }

    @GetMapping("/test")
    String test(){
        return "test";
    }
}
