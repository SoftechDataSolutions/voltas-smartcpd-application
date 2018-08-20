package com.softech.dev.repository.search;

import com.softech.dev.domain.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Question entity.
 */
public interface QuestionSearchRepository extends ElasticsearchRepository<Question, Long> {
}
