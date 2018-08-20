package com.softech.dev.repository.search;

import com.softech.dev.domain.Choice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Choice entity.
 */
public interface ChoiceSearchRepository extends ElasticsearchRepository<Choice, Long> {
}
