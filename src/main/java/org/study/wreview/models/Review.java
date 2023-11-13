package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "reviews")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    int id;

    @Column(name = "time_stamp")
    Date dateTime;

    @ManyToOne
    @JoinColumn(name = "caller", referencedColumnName = "username")
    Caller caller;

    @Column(name = "equipment")
    @NotEmpty(message = "Поле 'оборудование' не должно быть пустым")
    @Max(value = 100, message = "Поле 'оборудование' должно содержать не более 100 символов")
    String equipment;

    @Column(name = "reason")
    @NotEmpty(message = "Поле 'причина вызова' не должно быть пустым")
    @Max(value = 500, message = "Поле 'причина вызова' должно содержать не более 500 символов")
    String reason;

    @ManyToOne
    @JoinColumn(name = "worker", referencedColumnName = "name")
    Worker worker;

    @Column(name = "work_done")
    @NotEmpty(message = "Метка 'работа выполнена' должна быть определена")
    boolean workDone;

    @Column(name = "comment")
    String comment;

    @Column(name = "rating")
    @NotEmpty(message = "Вы должны поставить оценку рабочему")
    @Size(min = 0, max = 10, message = "Поставьте оценку от 0 до 10")
    int rating;
}
