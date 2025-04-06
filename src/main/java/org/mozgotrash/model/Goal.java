package org.mozgotrash.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Goal {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    @JoinColumn(name = "goal_id")
    List<Book> books;

    LocalDate deadline;
}
