package org.mozgotrash.repository;

import org.mozgotrash.model.Log;
import org.mozgotrash.repository.dto.GoalLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByBookId(Long bookId);

    @Query("SELECT NEW org.mozgotrash.repository.dto.GoalLogs(g.title, b.title, SUM(l.pageCount)) " +
            "FROM Log l " +
            "JOIN l.book b " +
            "JOIN b.goal g " +
            "WHERE l.logDate > :startDate AND g.user.id = :userId " +
            "GROUP BY g.title, b.title")
    List<GoalLogs> getLogsByGoalsForUser(@Param("userId") Long userId, @Param("startDate") LocalDateTime since);
}
