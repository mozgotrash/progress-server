package org.mozgotrash.repository;

import org.mozgotrash.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByBookId(Long bookId);
}
