package com.softech.dev.repository;

import com.softech.dev.domain.Topic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Topic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {

}
