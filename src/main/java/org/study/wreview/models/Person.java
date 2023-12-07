package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.study.wreview.utils.CurrentUserInfo;
import org.study.wreview.utils.DateUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString(exclude = {"myReviews", "reviewsOnMe"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person {
    @Id
    @Size(min = 2, max = 50, message = "Поле 'ФИО' должно содержать от 2 до 50 символов")
    String username;

    String password;

    @NotNull(message = "Поле 'дата рождения' не должно быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    LocalDate birthday;

    @NotEmpty(message = "Поле 'телефон' не должно быть пустым")
    @Size(max = 20, message = "Поле 'телефон' должно содержать не более 20 символов")
    String phone;

    @Column(name = "i_am_worker")
    boolean iamWorker;

    @Column(name = "service_description")
    @Size(max = 500, message = "Поле 'описание услуг' должно содержать не более 500 символов")
    String serviceDescription;

    @Column(name = "experience_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    LocalDate experienceDate;

    @Min(value = 0, message = "Поле 'цена услуги' не может быть ниже 0")
    @Max(value = 1000000, message = "Поле 'цена услуги' не может быть выше 1 000 000")
    Integer price;

    boolean enabled;

    String role;

    @OneToMany(mappedBy = "caller", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> myReviews;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviewsOnMe;

    @Transient
    Double rating;

    @Transient
    Long numOfCalculatedReviews;


    public Person(String username){
        this.username = username;
        this.birthday = LocalDate.now();
        this.iamWorker = false;
        this.enabled = false;
        this.role = "ROLE_GUEST";
        this.phone = "0";
    }

    public int getExperience() {
        return DateUtils.getDuration(experienceDate);
    }

    public int getAge() {
        return DateUtils.getDuration(birthday);
    }

    public Double getRating(){
        if(reviewsOnMe == null){
            return 0.0;
        }
        if (rating == null){
            rating = reviewsOnMe.stream()
                    .filter(review -> review.getTimestamp().isAfter(DateUtils.getDateTimeAgo()))
                    .mapToDouble(Review::getRating).average().orElse(0.0);
        }
        return rating;
    }

    public String getStringRating(){
        if(reviewsOnMe == null){
            return new DecimalFormat("#0.0#").format(0.0);
        }
        return new DecimalFormat("#0.0#").format(getRating());
    }

    public long getNumOfCalcReviews(){
        if(reviewsOnMe == null){
            return 0;
        }
        if(numOfCalculatedReviews == null){
            numOfCalculatedReviews = reviewsOnMe.stream()
                    .filter(review -> review.getTimestamp().isAfter(DateUtils.getDateTimeAgo()))
                    .count();
        }
        return numOfCalculatedReviews;
    }

    public String getSmallServiceDescription(){
        String result = serviceDescription.substring(0, Math.min(serviceDescription.length(), 20));
        return result.length() == 20? result + "...": result;
    }

    public boolean currentUserIsMe(){
        return CurrentUserInfo.userIsCurrent(getUsername());
    }

    public boolean currentUserNotMe(){
        return !currentUserIsMe();
    }

    public boolean isAdmin(){
        return getRole().equals("ROLE_ADMIN");
    }

    public boolean isUser(){
        return getRole().equals("ROLE_USER");
    }
}
