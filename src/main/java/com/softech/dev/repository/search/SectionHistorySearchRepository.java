package com.softech.dev.repository.search;

import com.softech.dev.domain.SectionHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SectionHistory entity.
 */
public interface SectionHistorySearchRepository extends ElasticsearchRepository<SectionHistory, Long> {
}
