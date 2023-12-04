package org.study.wreview.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.study.wreview.models.Person;
import org.study.wreview.models.Review;
import org.study.wreview.services.PersonService;
import org.study.wreview.services.ReviewService;
import org.study.wreview.utils.CurrentUserInfo;
import org.study.wreview.utils.PaginationFilterEngine;

import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping("/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewController implements PaginationFilterEngine {

    ReviewService reviewService;
    PersonService personService;

    @GetMapping("")
    public String index(Model model,
                        @RequestParam(name = "pageNum", required = false) Integer pageNum,
                        @RequestParam(name = "filter", required = false) String filter) {

        model.addAttribute("reviews", getPage(model, pageNum, filter,
                Sort.by("timestamp").descending(),
                ((pageable, f) -> reviewService.findAllWithFilter(f, pageable))
        ));

        return "review/index";
    }

    @GetMapping("/my")
    public String indexMy(Model model,
                          @RequestParam(name = "pageNum", required = false) Integer pageNum,
                          @RequestParam(name = "filter", required = false) String filter) {

        model.addAttribute("reviews", getPage(model, pageNum, filter,
                Sort.by("timestamp").descending(),
                ((pageable, f) -> reviewService.findAllByCaller(CurrentUserInfo.getUsername(), f, pageable))
        ));

        return "review/index";
    }

    @GetMapping("/on_me")
    public String indexOnMe(Model model,
                            @RequestParam(name = "pageNum", required = false) Integer pageNum,
                            @RequestParam(name = "filter", required = false) String filter) {

        model.addAttribute("reviews", getPage(model, pageNum, filter,
                Sort.by("timestamp").descending(),
                ((pageable, f) -> reviewService.findAllByWorker(CurrentUserInfo.getUsername(), f, pageable))
        ));

        return "review/index";
    }

    @GetMapping("/add")
    public String addGet(@ModelAttribute("review") Review review, Model model,
                         @RequestParam(value = "workerName", required = false) String workerName) {

        personService.findByUsername(CurrentUserInfo.getUsername()).ifPresentOrElse(
                review::setCaller,
                () -> review.setCaller(new Person(CurrentUserInfo.getUsername()))
        );

        personService.findByUsername(workerName).ifPresent(review::setWorker);

        review.setTimestamp(new Date());
        model.addAttribute("workers", personService.findWorkers());
        return "review/add";
    }

    @PostMapping("")
    public String addPost(@ModelAttribute("review") @Valid Review review, BindingResult bindingResult, Model model) {

        extendValidationConditions(review, model, bindingResult);

        if (bindingResult.hasErrors()) {
            return "review/add";
        }
        reviewService.save(review);
        return "redirect:/reviews";
    }

    @GetMapping("/{id}")
    public String info(Model model,
                          @PathVariable(name = "id") long id) {

        reviewService.findById(id).ifPresent(r -> model.addAttribute("review", r));
        return "review/info";
    }

    @GetMapping("/{id}/edit")
    public String editGet(Model model,
                          @PathVariable(name = "id") long id) {

        Optional<Review> review = reviewService.findById(id);
        if (review.isEmpty()) {
            return "error";
        }
        model.addAttribute("review", review.get());
        model.addAttribute("workers", personService.findWorkers());

        return "review/edit";
    }

    @PatchMapping("/{id}")
    public String editPost(@ModelAttribute @Valid Review review,
                           BindingResult bindingResult,
                           @PathVariable(name = "id") long id,
                           Model model) {

        Optional<Review> reviewForUpdate = reviewService.findById(id);
        if (reviewForUpdate.isEmpty()) {
            return "error";
        }

        extendValidationConditions(review, model, bindingResult);

        if (!CurrentUserInfo.currentUserIsAdmin() && !CurrentUserInfo.userIsCurrent(review.getCaller().getUsername())) {
            bindingResult.rejectValue("caller.username", "",
                    "У вас нет права редактировать отзыв");
        }

        if (bindingResult.hasErrors()) {
            return "review/edit";
        }
        reviewService.update(id, review);

        return "redirect:/reviews";
    }

    @DeleteMapping("/{id}")
    public String info(@PathVariable("id") long id){
        Optional<Review> review = reviewService.findById(id);
        if(review.isEmpty() || !review.get().currentUserIsCallerOrAdmin()) {
            return "error";
        }
        reviewService.delete(review.get());
        return "redirect:/reviews";
    }

    private void extendValidationConditions(Review review, Model model, BindingResult bindingResult){
        Optional<Person> person = personService.findWorkerByUsername(review.getWorker().getUsername());
        if (person.isEmpty() || review.callerAndWorkerSame()){
            model.addAttribute("workers", personService.findWorkers());
            bindingResult.rejectValue("worker.username", "","Введите имя рабочего правильно");
        }
        int rating = review.getRating() == null? 0: review.getRating();
        if(!review.isWorkDone() && rating > 5){
            bindingResult.rejectValue("rating", "",
                    "Если работа не выполнена, рейтинг не может быть выше 5");
        }
    }
}
