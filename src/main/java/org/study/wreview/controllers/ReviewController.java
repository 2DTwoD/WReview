package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.study.wreview.models.Person;
import org.study.wreview.models.Review;
import org.study.wreview.services.PersonService;
import org.study.wreview.services.ReviewService;
import org.study.wreview.utils.CurrentUserInfo;
import org.study.wreview.utils.Sorting;

import java.util.Date;


@Controller
@RequestMapping("/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewController {
    ReviewService reviewService;
    PersonService personService;
    CurrentUserInfo currentUserInfo;
    @GetMapping("")
    public String index(Model model){
        model.addAttribute("reviews", reviewService.findAll());
        return "review/index";
    }

    @GetMapping("/add")
    public String addGet(@ModelAttribute("review") Review review, Model model){
        personService.findByUsername(currentUserInfo.getUsername()).ifPresentOrElse(
                review::setCaller,
                () -> review.setCaller(new Person(currentUserInfo.getUsername()))
        );
        review.setTimestamp(new Date());
        model.addAttribute("workers", personService.findWorkers(Sorting.NAME_ASC));
        return "review/add";
    }

    @PostMapping("")
    public String addPost(@ModelAttribute("review") @Valid Review review, BindingResult bindingResult, Model model){
        personService.findWorkerByUsername(review.getWorker().getUsername())
                .ifPresentOrElse(
                        review::setWorker,
                        () -> {
                            model.addAttribute("workers", personService.findWorkers(Sorting.NAME_ASC));
                            bindingResult.rejectValue("worker", "",
                                    "Введите имя рабочего правильно");
                        });
        if(bindingResult.hasErrors()){
            return "review/add";
        }
        personService.findByUsername(currentUserInfo.getUsername()).ifPresentOrElse(
                review::setCaller,
                () -> {
                    Person person = new Person(currentUserInfo.getUsername());
                    personService.save(person);
                    review.setCaller(person);
                });
        reviewService.save(review);
        return "redirect:/review";
    }
}
