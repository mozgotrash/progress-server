package org.mozgotrash.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Book {

    @Id
    @GeneratedValue
    Long id;

    String author;

    String title;

    Integer pageCount;

    @ManyToOne
    Goal goal;
}
