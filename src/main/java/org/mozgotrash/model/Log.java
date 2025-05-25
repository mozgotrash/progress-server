package org.mozgotrash.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Log {
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Integer pageCount;

    @ManyToOne
    Book book;

    @Column(nullable = false)
    @CreationTimestamp
    LocalDateTime logDate;

}
