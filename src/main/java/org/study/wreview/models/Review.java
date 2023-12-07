package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.study.wreview.utils.CurrentUserInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@ToString
@NoArgsConstructor
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime timestamp;

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

    @Size(max = 500, message = "Поле 'комментарий' должно содержать не более 500 символов")
    String comment;

    @Min(value = 0, message = "Оценка не должна быть ниже 0")
    @Max(value = 10, message = "Оценка не должна быть выше 10")
    @NotNull(message = "Это поле не должно быть пустым")
    Integer rating;

    public String getBeautyTimestamp(){
        return getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getShortComment(){
        return getShortString(getComment());
    }

    public String getShortReason(){
        return getShortString(getReason());
    }

    public String getShortEquipment(){
         return getShortString(getEquipment());
    }

    private String getShortString(String str){
        if (str == null){
            return "";
        }
        String result = str.substring(0, Math.min(str.length(), 20));
        return result.length() == 20? result + "...": result;
    }

    public boolean currentUserIsCallerOrAdmin(){
        return CurrentUserInfo.currentUserIsAdmin() || CurrentUserInfo.getUsername().equals(getCaller().getUsername());
    }

    public boolean callerAndWorkerSame(){
        return getWorker().getUsername().equals(getCaller().getUsername());
    }
}
