package org.mozgotrash.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@Entity
public class Log {
    @Id
    @GeneratedValue
    Long id;

    Integer pageCount;

    @ManyToOne
    Book book;

    OffsetDateTime logDate;

}
