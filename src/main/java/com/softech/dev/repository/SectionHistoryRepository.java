package com.softech.dev.repository;

import com.softech.dev.domain.SectionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SectionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionHistoryRepository extends JpaRepository<SectionHistory, Long>, JpaSpecificationExecutor<SectionHistory> {

}
