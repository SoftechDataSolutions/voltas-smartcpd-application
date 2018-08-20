package com.softech.dev.repository.search;

import com.softech.dev.domain.CourseCartBridge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourseCartBridge entity.
 */
public interface CourseCartBridgeSearchRepository extends ElasticsearchRepository<CourseCartBridge, Long> {
}
