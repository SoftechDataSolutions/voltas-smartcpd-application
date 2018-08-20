package com.softech.dev.repository.search;

import com.softech.dev.domain.QuestionHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the QuestionHistory entity.
 */
public interface QuestionHistorySearchRepository extends ElasticsearchRepository<QuestionHistory, Long> {
}
