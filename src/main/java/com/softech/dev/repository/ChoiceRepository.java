package com.softech.dev.repository;

import com.softech.dev.domain.Choice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Choice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long>, JpaSpecificationExecutor<Choice> {

}
