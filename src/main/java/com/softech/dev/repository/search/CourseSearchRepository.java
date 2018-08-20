package com.softech.dev.repository.search;

import com.softech.dev.domain.Course;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Course entity.
 */
public interface CourseSearchRepository extends ElasticsearchRepository<Course, Long> {
}
