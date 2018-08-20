package com.softech.dev.repository.search;

import com.softech.dev.domain.Quiz;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Quiz entity.
 */
public interface QuizSearchRepository extends ElasticsearchRepository<Quiz, Long> {
}
