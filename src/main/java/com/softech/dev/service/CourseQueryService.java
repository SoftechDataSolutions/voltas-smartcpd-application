package com.softech.dev.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.softech.dev.domain.Course;
import com.softech.dev.domain.*; // for static metamodels
import com.softech.dev.repository.CourseRepository;
import com.softech.dev.repository.search.CourseSearchRepository;
import com.softech.dev.service.dto.CourseCriteria;


/**
 * Service for executing complex queries for Course entities in the database.
 * The main input is a {@link CourseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Course} or a {@link Page} of {@link Course} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseQueryService extends QueryService<Course> {

    private final Logger log = LoggerFactory.getLogger(CourseQueryService.class);

    private final CourseRepository courseRepository;

    private final CourseSearchRepository courseSearchRepository;

    public CourseQueryService(CourseRepository courseRepository, CourseSearchRepository courseSearchRepository) {
        this.courseRepository = courseRepository;
        this.courseSearchRepository = courseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Course} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Course> findByCriteria(CourseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Course} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Course> findByCriteria(CourseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification, page);
    }

    /**
     * Function to convert CourseCriteria to a {@link Specification}
     */
    private Specification<Course> createSpecification(CourseCriteria criteria) {
        Specification<Course> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Course_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Course_.title));
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSection(), Course_.section));
            }
            if (criteria.getNormCourses() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNormCourses(), Course_.normCourses));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Course_.description));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Course_.amount));
            }
            if (criteria.getStartdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartdate(), Course_.startdate));
            }
            if (criteria.getEnddate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnddate(), Course_.enddate));
            }
            if (criteria.getPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoint(), Course_.point));
            }
            if (criteria.getCredit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCredit(), Course_.credit));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Course_.country));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Course_.state));
            }
            if (criteria.getTopicId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTopicId(), Course_.topic, Topic_.id));
            }
        }
        return specification;
    }

}
