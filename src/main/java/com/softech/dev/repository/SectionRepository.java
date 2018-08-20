package com.softech.dev.repository;

import com.softech.dev.domain.Section;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Section entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionRepository extends JpaRepository<Section, Long>, JpaSpecificationExecutor<Section> {

}
