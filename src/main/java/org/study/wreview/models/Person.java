package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString
public class Person{
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

    public Person(String username){
        this.username = username;
        this.birthday = new Date();
        this.iamWorker = false;
        this.enabled = false;
        this.role = "ROLE_GUEST";
        this.phone = "0";
    }
}
