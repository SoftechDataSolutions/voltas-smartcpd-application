package com.softech.dev.repository.search;

import com.softech.dev.domain.Section;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Section entity.
 */
public interface SectionSearchRepository extends ElasticsearchRepository<Section, Long> {
}
