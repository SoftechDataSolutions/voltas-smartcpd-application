package com.softech.dev.repository;

import com.softech.dev.domain.QuizHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuizHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long>, JpaSpecificationExecutor<QuizHistory> {

}
