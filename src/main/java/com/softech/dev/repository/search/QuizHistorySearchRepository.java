package com.softech.dev.repository.search;

import com.softech.dev.domain.QuizHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the QuizHistory entity.
 */
public interface QuizHistorySearchRepository extends ElasticsearchRepository<QuizHistory, Long> {
}
