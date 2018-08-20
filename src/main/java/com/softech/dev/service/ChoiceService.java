package com.softech.dev.service;

import com.softech.dev.domain.Choice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Choice.
 */
public interface ChoiceService {

    /**
     * Save a choice.
     *
     * @param choice the entity to save
     * @return the persisted entity
     */
    Choice save(Choice choice);

    /**
     * Get all the choices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Choice> findAll(Pageable pageable);


    /**
     * Get the "id" choice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Choice> findOne(Long id);

    /**
     * Delete the "id" choice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the choice corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Choice> search(String query, Pageable pageable);
}
