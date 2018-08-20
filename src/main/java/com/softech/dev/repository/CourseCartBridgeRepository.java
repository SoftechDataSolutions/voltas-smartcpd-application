package com.softech.dev.repository;

import com.softech.dev.domain.CourseCartBridge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseCartBridge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseCartBridgeRepository extends JpaRepository<CourseCartBridge, Long>, JpaSpecificationExecutor<CourseCartBridge> {

}
