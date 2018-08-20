package com.softech.dev.service;

import com.softech.dev.domain.QuizHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing QuizHistory.
 */
public interface QuizHistoryService {

    /**
     * Save a quizHistory.
     *
     * @param quizHistory the entity to save
     * @return the persisted entity
     */
    QuizHistory save(QuizHistory quizHistory);

    /**
     * Get all the quizHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuizHistory> findAll(Pageable pageable);


    /**
     * Get the "id" quizHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<QuizHistory> findOne(Long id);

    /**
     * Delete the "id" quizHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the quizHistory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuizHistory> search(String query, Pageable pageable);
}
