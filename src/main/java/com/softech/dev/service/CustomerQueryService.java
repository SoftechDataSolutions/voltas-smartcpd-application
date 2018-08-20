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

import com.softech.dev.domain.Customer;
import com.softech.dev.domain.*; // for static metamodels
import com.softech.dev.repository.CustomerRepository;
import com.softech.dev.repository.search.CustomerSearchRepository;
import com.softech.dev.service.dto.CustomerCriteria;


/**
 * Service for executing complex queries for Customer entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Customer} or a {@link Page} of {@link Customer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    private final CustomerSearchRepository customerSearchRepository;

    public CustomerQueryService(CustomerRepository customerRepository, CustomerSearchRepository customerSearchRepository) {
        this.customerRepository = customerRepository;
        this.customerSearchRepository = customerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Customer} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Customer> findByCriteria(CustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Customer} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Customer> findByCriteria(CustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page);
    }

    /**
     * Function to convert CustomerCriteria to a {@link Specification}
     */
    private Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Customer_.id));
            }
            if (criteria.getNormalized() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNormalized(), Customer_.normalized));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Customer_.phone));
            }
            if (criteria.getStreetaddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetaddress(), Customer_.streetaddress));
            }
            if (criteria.getPostalcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalcode(), Customer_.postalcode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Customer_.city));
            }
            if (criteria.getStateProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateProvince(), Customer_.stateProvince));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Customer_.country));
            }
            if (criteria.getRegistered() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistered(), Customer_.registered));
            }
            if (criteria.getLastactive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastactive(), Customer_.lastactive));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), Customer_.points));
            }
            if (criteria.getCycledate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCycledate(), Customer_.cycledate));
            }
            if (criteria.getAreaserviced() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAreaserviced(), Customer_.areaserviced));
            }
            if (criteria.getSpecialities() != null) {
                specification = specification.and(buildSpecification(criteria.getSpecialities(), Customer_.specialities));
            }
            if (criteria.getTrades() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrades(), Customer_.trades));
            }
            if (criteria.getMonthYear() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMonthYear(), Customer_.monthYear));
            }
            if (criteria.getLicenseNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenseNumber(), Customer_.licenseNumber));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCompanyId(), Customer_.company, Company_.id));
            }
        }
        return specification;
    }

}
