package com.softech.dev.repository.search;

import com.softech.dev.domain.Certificate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Certificate entity.
 */
public interface CertificateSearchRepository extends ElasticsearchRepository<Certificate, Long> {
}
