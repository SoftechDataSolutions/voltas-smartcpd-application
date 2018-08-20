package com.softech.dev.web.rest;

import com.softech.dev.WebinarappApp;

import com.softech.dev.domain.Certificate;
import com.softech.dev.domain.Customer;
import com.softech.dev.repository.CertificateRepository;
import com.softech.dev.repository.search.CertificateSearchRepository;
import com.softech.dev.service.CertificateService;
import com.softech.dev.web.rest.errors.ExceptionTranslator;
import com.softech.dev.service.dto.CertificateCriteria;
import com.softech.dev.service.CertificateQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.softech.dev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CertificateResource REST controller.
 *
 * @see CertificateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebinarappApp.class)
public class CertificateResourceIntTest {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private CertificateRepository certificateRepository;

    

    @Autowired
    private CertificateService certificateService;

    /**
     * This repository is mocked in the com.softech.dev.repository.search test package.
     *
     * @see com.softech.dev.repository.search.CertificateSearchRepositoryMockConfiguration
     */
    @Autowired
    private CertificateSearchRepository mockCertificateSearchRepository;

    @Autowired
    private CertificateQueryService certificateQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCertificateMockMvc;

    private Certificate certificate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CertificateResource certificateResource = new CertificateResource(certificateService, certificateQueryService);
        this.restCertificateMockMvc = MockMvcBuilders.standaloneSetup(certificateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .timestamp(DEFAULT_TIMESTAMP)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .course(DEFAULT_COURSE)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return certificate;
    }

    @Before
    public void initTest() {
        certificate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCertificate() throws Exception {
        int databaseSizeBeforeCreate = certificateRepository.findAll().size();

        // Create the Certificate
        restCertificateMockMvc.perform(post("/api/certificates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isCreated());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeCreate + 1);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testCertificate.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testCertificate.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testCertificate.getCourse()).isEqualTo(DEFAULT_COURSE);
        assertThat(testCertificate.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCertificate.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);

        // Validate the Certificate in Elasticsearch
        verify(mockCertificateSearchRepository, times(1)).save(testCertificate);
    }

    @Test
    @Transactional
    public void createCertificateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = certificateRepository.findAll().size();

        // Create the Certificate with an existing ID
        certificate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCertificateMockMvc.perform(post("/api/certificates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeCreate);

        // Validate the Certificate in Elasticsearch
        verify(mockCertificateSearchRepository, times(0)).save(certificate);
    }

    @Test
    @Transactional
    public void getAllCertificates() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList
        restCertificateMockMvc.perform(get("/api/certificates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].course").value(hasItem(DEFAULT_COURSE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }
    

    @Test
    @Transactional
    public void getCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get the certificate
        restCertificateMockMvc.perform(get("/api/certificates/{id}", certificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(certificate.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.course").value(DEFAULT_COURSE.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getAllCertificatesByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where timestamp equals to DEFAULT_TIMESTAMP
        defaultCertificateShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the certificateList where timestamp equals to UPDATED_TIMESTAMP
        defaultCertificateShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllCertificatesByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultCertificateShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the certificateList where timestamp equals to UPDATED_TIMESTAMP
        defaultCertificateShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllCertificatesByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where timestamp is not null
        defaultCertificateShouldBeFound("timestamp.specified=true");

        // Get all the certificateList where timestamp is null
        defaultCertificateShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllCertificatesByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where firstname equals to DEFAULT_FIRSTNAME
        defaultCertificateShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the certificateList where firstname equals to UPDATED_FIRSTNAME
        defaultCertificateShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllCertificatesByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultCertificateShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the certificateList where firstname equals to UPDATED_FIRSTNAME
        defaultCertificateShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllCertificatesByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where firstname is not null
        defaultCertificateShouldBeFound("firstname.specified=true");

        // Get all the certificateList where firstname is null
        defaultCertificateShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    public void getAllCertificatesByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where lastname equals to DEFAULT_LASTNAME
        defaultCertificateShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the certificateList where lastname equals to UPDATED_LASTNAME
        defaultCertificateShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllCertificatesByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultCertificateShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the certificateList where lastname equals to UPDATED_LASTNAME
        defaultCertificateShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllCertificatesByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where lastname is not null
        defaultCertificateShouldBeFound("lastname.specified=true");

        // Get all the certificateList where lastname is null
        defaultCertificateShouldNotBeFound("lastname.specified=false");
    }

    @Test
    @Transactional
    public void getAllCertificatesByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where course equals to DEFAULT_COURSE
        defaultCertificateShouldBeFound("course.equals=" + DEFAULT_COURSE);

        // Get all the certificateList where course equals to UPDATED_COURSE
        defaultCertificateShouldNotBeFound("course.equals=" + UPDATED_COURSE);
    }

    @Test
    @Transactional
    public void getAllCertificatesByCourseIsInShouldWork() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where course in DEFAULT_COURSE or UPDATED_COURSE
        defaultCertificateShouldBeFound("course.in=" + DEFAULT_COURSE + "," + UPDATED_COURSE);

        // Get all the certificateList where course equals to UPDATED_COURSE
        defaultCertificateShouldNotBeFound("course.in=" + UPDATED_COURSE);
    }

    @Test
    @Transactional
    public void getAllCertificatesByCourseIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList where course is not null
        defaultCertificateShouldBeFound("course.specified=true");

        // Get all the certificateList where course is null
        defaultCertificateShouldNotBeFound("course.specified=false");
    }

    @Test
    @Transactional
    public void getAllCertificatesByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        certificate.setCustomer(customer);
        certificateRepository.saveAndFlush(certificate);
        Long customerId = customer.getId();

        // Get all the certificateList where customer equals to customerId
        defaultCertificateShouldBeFound("customerId.equals=" + customerId);

        // Get all the certificateList where customer equals to customerId + 1
        defaultCertificateShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCertificateShouldBeFound(String filter) throws Exception {
        restCertificateMockMvc.perform(get("/api/certificates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].course").value(hasItem(DEFAULT_COURSE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCertificateShouldNotBeFound(String filter) throws Exception {
        restCertificateMockMvc.perform(get("/api/certificates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCertificate() throws Exception {
        // Get the certificate
        restCertificateMockMvc.perform(get("/api/certificates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCertificate() throws Exception {
        // Initialize the database
        certificateService.save(certificate);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCertificateSearchRepository);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Update the certificate
        Certificate updatedCertificate = certificateRepository.findById(certificate.getId()).get();
        // Disconnect from session so that the updates on updatedCertificate are not directly saved in db
        em.detach(updatedCertificate);
        updatedCertificate
            .timestamp(UPDATED_TIMESTAMP)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .course(UPDATED_COURSE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);

        restCertificateMockMvc.perform(put("/api/certificates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCertificate)))
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testCertificate.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testCertificate.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCertificate.getCourse()).isEqualTo(UPDATED_COURSE);
        assertThat(testCertificate.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCertificate.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);

        // Validate the Certificate in Elasticsearch
        verify(mockCertificateSearchRepository, times(1)).save(testCertificate);
    }

    @Test
    @Transactional
    public void updateNonExistingCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Create the Certificate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCertificateMockMvc.perform(put("/api/certificates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Certificate in Elasticsearch
        verify(mockCertificateSearchRepository, times(0)).save(certificate);
    }

    @Test
    @Transactional
    public void deleteCertificate() throws Exception {
        // Initialize the database
        certificateService.save(certificate);

        int databaseSizeBeforeDelete = certificateRepository.findAll().size();

        // Get the certificate
        restCertificateMockMvc.perform(delete("/api/certificates/{id}", certificate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Certificate in Elasticsearch
        verify(mockCertificateSearchRepository, times(1)).deleteById(certificate.getId());
    }

    @Test
    @Transactional
    public void searchCertificate() throws Exception {
        // Initialize the database
        certificateService.save(certificate);
        when(mockCertificateSearchRepository.search(queryStringQuery("id:" + certificate.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(certificate), PageRequest.of(0, 1), 1));
        // Search the certificate
        restCertificateMockMvc.perform(get("/api/_search/certificates?query=id:" + certificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].course").value(hasItem(DEFAULT_COURSE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Certificate.class);
        Certificate certificate1 = new Certificate();
        certificate1.setId(1L);
        Certificate certificate2 = new Certificate();
        certificate2.setId(certificate1.getId());
        assertThat(certificate1).isEqualTo(certificate2);
        certificate2.setId(2L);
        assertThat(certificate1).isNotEqualTo(certificate2);
        certificate1.setId(null);
        assertThat(certificate1).isNotEqualTo(certificate2);
    }
}
