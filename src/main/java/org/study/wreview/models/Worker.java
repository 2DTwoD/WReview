package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "workers")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString(exclude = {"reviews"})
public class Worker {
    @Id
    @Column(name = "name")
    @NotEmpty(message = "Поле 'имя' не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    String name;

    @Column(name = "position")
    @NotEmpty(message = "Поле 'должность' не должно быть пустым")
    @Size(min = 2, max = 50, message = "Должность должна содержать от 2 до 100 символов")
    String position;

    @Column(name = "empl_date")
    @NotEmpty(message = "Поле 'дата приема на работу' не должно быть пустым")
    Date emplDate;

    @OneToMany(mappedBy = "worker")
    List<Review> reviews;
}
