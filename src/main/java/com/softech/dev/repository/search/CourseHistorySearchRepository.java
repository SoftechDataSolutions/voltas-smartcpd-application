package com.softech.dev.repository.search;

import com.softech.dev.domain.CourseHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourseHistory entity.
 */
public interface CourseHistorySearchRepository extends ElasticsearchRepository<CourseHistory, Long> {
}
