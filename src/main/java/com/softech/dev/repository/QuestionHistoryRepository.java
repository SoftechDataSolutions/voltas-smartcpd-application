package com.softech.dev.repository;

import com.softech.dev.domain.QuestionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuestionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long>, JpaSpecificationExecutor<QuestionHistory> {

}
