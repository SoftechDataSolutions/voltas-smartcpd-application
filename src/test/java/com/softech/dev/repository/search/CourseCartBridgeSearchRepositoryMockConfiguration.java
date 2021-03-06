package com.softech.dev.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CourseCartBridgeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CourseCartBridgeSearchRepositoryMockConfiguration {

    @MockBean
    private CourseCartBridgeSearchRepository mockCourseCartBridgeSearchRepository;

}
