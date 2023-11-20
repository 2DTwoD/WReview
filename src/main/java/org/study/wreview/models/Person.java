package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.study.wreview.utils.DateUtils;


import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString
public class Person {
    @Id
    @Size(min = 2, max = 50, message = "Поле 'ФИО' должно содержать от 2 до 50 символов")
    String username;
    String password;

    @NotNull(message = "Поле 'дата рождения' не должно быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @NotEmpty(message = "Поле 'телефон' не должно быть пустым")
    String phone;

    @Column(name = "i_am_worker")
    boolean iamWorker;

    @Column(name = "service_description")
    String serviceDescription;

    @Column(name = "experience_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date experienceDate;

    int price;

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
        this.birthday = new Date(System.currentTimeMillis());
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

    public double getRating(){
        if(reviewsOnMe == null){
            return 0.0;
        }
        if (rating == null){
            rating = reviewsOnMe.stream()
                    .filter(review -> review.getTimestamp().after(DateUtils.getDateAgo()))
                    .mapToDouble(Review::getRating).average().orElse(0.0);
        }
        return rating;
    }
    public long getNumOfCalcReviews(){
        if(reviewsOnMe == null){
            return 0;
        }
        if(numOfCalculatedReviews == null){
            numOfCalculatedReviews = reviewsOnMe.stream()
                    .filter(review -> review.getTimestamp().after(DateUtils.getDateAgo()))
                    .count();
        }
        return numOfCalculatedReviews;
    }
}
