package com.softech.dev.repository.search;

import com.softech.dev.domain.Cart;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cart entity.
 */
public interface CartSearchRepository extends ElasticsearchRepository<Cart, Long> {
}
