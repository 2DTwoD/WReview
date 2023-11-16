package org.study.wreview.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users_with_rating")
public class PersonWithRating {
    @Id
    String username;

    String phone;

    @Column(name = "service_description")
    String serviceDescription;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    Date birthday;

    @Column(name = "experience_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    Date experienceDate;

    int price;

    @Column(name = "avg_rating")
    Double rating;

    public int getExperience(){
        return getDuration(experienceDate);
    }

    public int getAge(){
        return getDuration(birthday);
    }

    private int getDuration(Date date){
        LocalDate lDate1 = LocalDate.now();
        LocalDate lDate2 = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return lDate1.until(lDate2).getYears();
    }
}
