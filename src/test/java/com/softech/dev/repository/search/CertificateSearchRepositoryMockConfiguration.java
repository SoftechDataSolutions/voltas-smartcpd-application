package com.softech.dev.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CertificateSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CertificateSearchRepositoryMockConfiguration {

    @MockBean
    private CertificateSearchRepository mockCertificateSearchRepository;

}
