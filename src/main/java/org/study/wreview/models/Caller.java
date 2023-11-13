package org.study.wreview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "callers")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString(exclude = {"reviews"})
public class Caller {
    @Id
    @Column(name = "username")
    @NotEmpty(message = "Поле 'имя' не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "role")
    String role;

    @NotEmpty(message = "Поле 'должность' не должно быть пустым")
    @Size(min = 2, max = 50, message = "Должность должна содержать от 2 до 100 символов")
    @Column(name = "position")
    String position;

    @Column(name = "enabled")
    boolean enabled;

    @OneToMany(mappedBy = "caller")
    List<Review> reviews;
}
