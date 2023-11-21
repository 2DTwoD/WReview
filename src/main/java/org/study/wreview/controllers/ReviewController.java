package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.Person;
import org.study.wreview.models.Review;
import org.study.wreview.services.PersonService;
import org.study.wreview.services.ReviewService;
import org.study.wreview.utils.Constants;
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
    Constants constants;

    @GetMapping("")
    public String index(Model model, @RequestParam(name = "pageNum", required = false) Integer pageNum){
        int currentPage = 0;
        if(pageNum != null){
            currentPage = Math.max(0, pageNum);
        }
        Pageable pages = PageRequest.of(currentPage, constants.rowsInPage, Sort.by("timestamp").descending());
        Page<Review> reviews = reviewService.findAll(pages);
        int totalPage = reviews.getTotalPages() - 1;
        currentPage = Math.max(0, Math.min(totalPage, currentPage));
        model.addAttribute("totalPages", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("reviews", reviews);
        return "review/index";
    }
    private void pageEngine(Model model, Integer pageNum){

    }

    @GetMapping("/my")
    public String indexMy(Model model){
        personService.findByUsername(currentUserInfo.getUsername())
                .ifPresent(person -> model.addAttribute("reviews", reviewService.findAllByCaller(person)));
        return "review/index";
    }
    @GetMapping("/on_me")
    public String indexOnMe(Model model){
        personService.findByUsername(currentUserInfo.getUsername())
                .ifPresent(person -> model.addAttribute("reviews", reviewService.findAllByWorker(person)));
        return "review/index";
    }


    @GetMapping("/add")
    public String addGet(@ModelAttribute("review") Review review, Model model,
                         @RequestParam(value = "workerName", required = false) String workerName){
        personService.findByUsername(currentUserInfo.getUsername()).ifPresentOrElse(
                review::setCaller,
                () -> review.setCaller(new Person(currentUserInfo.getUsername()))
        );
        personService.findByUsername(workerName).ifPresent(review::setWorker);
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
                            bindingResult.rejectValue("worker.username", "",
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
