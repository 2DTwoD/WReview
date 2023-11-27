package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.study.wreview.utils.CurrentUserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    Date timestamp;

    @ManyToOne
    @JoinColumn(name = "caller", referencedColumnName = "username")
    Person caller;

    @NotEmpty(message = "Поле 'оборудование' не должно быть пустым")
    @Size(max = 100, message = "Поле 'оборудование' должно содержать не более 100 символов")
    String equipment;

    @NotEmpty(message = "Поле 'причина вызова' не должно быть пустым")
    @Size(max = 500, message = "Поле 'причина вызова' должно содержать не более 500 символов")
    String reason;

    @ManyToOne
    @JoinColumn(name = "worker", referencedColumnName = "username")
    Person worker;

    @Column(name = "work_done")
    boolean workDone;

    String comment;

    @Min(value = 0, message = "Оценка не должна быть ниже 0")
    @Max(value = 10, message = "Оценка не должна быть выше 10")
    int rating;

    public String getBeautyTimestamp(){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(getTimestamp());
    }
    public String getSmallComment(){
        String result = comment.substring(0, Math.min(comment.length(), 20));
        return result.length() == 20? result + "...": result;
    }
    public boolean currentUserIsCallerOrAdmin(){
        return CurrentUserInfo.currentUserIsAdmin() || CurrentUserInfo.getUsername().equals(getCaller().getUsername());
    }
}
