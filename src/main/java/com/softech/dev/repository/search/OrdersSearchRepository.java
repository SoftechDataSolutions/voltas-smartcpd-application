package com.softech.dev.repository.search;

import com.softech.dev.domain.Orders;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Orders entity.
 */
public interface OrdersSearchRepository extends ElasticsearchRepository<Orders, Long> {
}
