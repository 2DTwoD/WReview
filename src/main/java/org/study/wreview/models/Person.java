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


import java.sql.Date;
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
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthday;

    @NotEmpty(message = "Поле 'телефон' не должно быть пустым")
    String phone;

    @Column(name = "i_am_worker")
    boolean iamWorker;

    @Column(name = "service_description")
    String serviceDescription;

    @Column(name = "experience_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date experienceDate;

    int price;

    boolean enabled;

    String role;

    @OneToMany(mappedBy = "caller")
    List<Review> myReviews;

    @OneToMany(mappedBy = "worker")
    List<Review> reviewsOnMe;

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
        return Math.round(100 * reviewsOnMe.stream()
                .filter(review -> review.getTimestamp().after(DateUtils.getDateAgo()))
                .mapToDouble(Review::getRating).average().orElse(0.0)) / 100.0;
    }
    public long getNumOfCalcReviews(){
        if(numOfCalculatedReviews == null){
            numOfCalculatedReviews = reviewsOnMe.stream()
                    .filter(review -> review.getTimestamp().after(DateUtils.getDateAgo()))
                    .count();
        }
        return numOfCalculatedReviews;
    }
}
