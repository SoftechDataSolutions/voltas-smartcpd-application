package com.softech.dev.repository.search;

import com.softech.dev.domain.TimeCourseLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TimeCourseLog entity.
 */
public interface TimeCourseLogSearchRepository extends ElasticsearchRepository<TimeCourseLog, Long> {
}
