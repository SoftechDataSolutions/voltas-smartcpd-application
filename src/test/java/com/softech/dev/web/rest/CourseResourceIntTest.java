package com.softech.dev.web.rest;

import com.softech.dev.WebinarappApp;

import com.softech.dev.domain.Course;
import com.softech.dev.domain.Topic;
import com.softech.dev.repository.CourseRepository;
import com.softech.dev.repository.search.CourseSearchRepository;
import com.softech.dev.service.CourseService;
import com.softech.dev.web.rest.errors.ExceptionTranslator;
import com.softech.dev.service.dto.CourseCriteria;
import com.softech.dev.service.CourseQueryService;

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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.softech.dev.web.rest.TestUtil.sameInstant;
import static com.softech.dev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourseResource REST controller.
 *
 * @see CourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebinarappApp.class)
public class CourseResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_NORM_COURSES = "AAAAAAAAAA";
    private static final String UPDATED_NORM_COURSES = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_STARTDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ENDDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENDDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_POINT = 1L;
    private static final Long UPDATED_POINT = 2L;

    private static final String DEFAULT_CREDIT = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    @Autowired
    private CourseRepository courseRepository;

    

    @Autowired
    private CourseService courseService;

    /**
     * This repository is mocked in the com.softech.dev.repository.search test package.
     *
     * @see com.softech.dev.repository.search.CourseSearchRepositoryMockConfiguration
     */
    @Autowired
    private CourseSearchRepository mockCourseSearchRepository;

    @Autowired
    private CourseQueryService courseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseMockMvc;

    private Course course;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseResource courseResource = new CourseResource(courseService, courseQueryService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .title(DEFAULT_TITLE)
            .section(DEFAULT_SECTION)
            .normCourses(DEFAULT_NORM_COURSES)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .startdate(DEFAULT_STARTDATE)
            .enddate(DEFAULT_ENDDATE)
            .point(DEFAULT_POINT)
            .credit(DEFAULT_CREDIT)
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE);
        return course;
    }

    @Before
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testCourse.getNormCourses()).isEqualTo(DEFAULT_NORM_COURSES);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testCourse.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCourse.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCourse.getStartdate()).isEqualTo(DEFAULT_STARTDATE);
        assertThat(testCourse.getEnddate()).isEqualTo(DEFAULT_ENDDATE);
        assertThat(testCourse.getPoint()).isEqualTo(DEFAULT_POINT);
        assertThat(testCourse.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testCourse.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCourse.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(1)).save(testCourse);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(0)).save(course);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setTitle(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setSection(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setDescription(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setAmount(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreditIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCredit(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCountry(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setState(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].normCourses").value(hasItem(DEFAULT_NORM_COURSES.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(sameInstant(DEFAULT_STARTDATE))))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(sameInstant(DEFAULT_ENDDATE))))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.intValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION.toString()))
            .andExpect(jsonPath("$.normCourses").value(DEFAULT_NORM_COURSES.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.startdate").value(sameInstant(DEFAULT_STARTDATE)))
            .andExpect(jsonPath("$.enddate").value(sameInstant(DEFAULT_ENDDATE)))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT.intValue()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title equals to DEFAULT_TITLE
        defaultCourseShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the courseList where title equals to UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCourseShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the courseList where title equals to UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title is not null
        defaultCourseShouldBeFound("title.specified=true");

        // Get all the courseList where title is null
        defaultCourseShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where section equals to DEFAULT_SECTION
        defaultCourseShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the courseList where section equals to UPDATED_SECTION
        defaultCourseShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllCoursesBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultCourseShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the courseList where section equals to UPDATED_SECTION
        defaultCourseShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllCoursesBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where section is not null
        defaultCourseShouldBeFound("section.specified=true");

        // Get all the courseList where section is null
        defaultCourseShouldNotBeFound("section.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByNormCoursesIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where normCourses equals to DEFAULT_NORM_COURSES
        defaultCourseShouldBeFound("normCourses.equals=" + DEFAULT_NORM_COURSES);

        // Get all the courseList where normCourses equals to UPDATED_NORM_COURSES
        defaultCourseShouldNotBeFound("normCourses.equals=" + UPDATED_NORM_COURSES);
    }

    @Test
    @Transactional
    public void getAllCoursesByNormCoursesIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where normCourses in DEFAULT_NORM_COURSES or UPDATED_NORM_COURSES
        defaultCourseShouldBeFound("normCourses.in=" + DEFAULT_NORM_COURSES + "," + UPDATED_NORM_COURSES);

        // Get all the courseList where normCourses equals to UPDATED_NORM_COURSES
        defaultCourseShouldNotBeFound("normCourses.in=" + UPDATED_NORM_COURSES);
    }

    @Test
    @Transactional
    public void getAllCoursesByNormCoursesIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where normCourses is not null
        defaultCourseShouldBeFound("normCourses.specified=true");

        // Get all the courseList where normCourses is null
        defaultCourseShouldNotBeFound("normCourses.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description equals to DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description is not null
        defaultCourseShouldBeFound("description.specified=true");

        // Get all the courseList where description is null
        defaultCourseShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where amount equals to DEFAULT_AMOUNT
        defaultCourseShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the courseList where amount equals to UPDATED_AMOUNT
        defaultCourseShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCoursesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultCourseShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the courseList where amount equals to UPDATED_AMOUNT
        defaultCourseShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCoursesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where amount is not null
        defaultCourseShouldBeFound("amount.specified=true");

        // Get all the courseList where amount is null
        defaultCourseShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByStartdateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startdate equals to DEFAULT_STARTDATE
        defaultCourseShouldBeFound("startdate.equals=" + DEFAULT_STARTDATE);

        // Get all the courseList where startdate equals to UPDATED_STARTDATE
        defaultCourseShouldNotBeFound("startdate.equals=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartdateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startdate in DEFAULT_STARTDATE or UPDATED_STARTDATE
        defaultCourseShouldBeFound("startdate.in=" + DEFAULT_STARTDATE + "," + UPDATED_STARTDATE);

        // Get all the courseList where startdate equals to UPDATED_STARTDATE
        defaultCourseShouldNotBeFound("startdate.in=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startdate is not null
        defaultCourseShouldBeFound("startdate.specified=true");

        // Get all the courseList where startdate is null
        defaultCourseShouldNotBeFound("startdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByStartdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startdate greater than or equals to DEFAULT_STARTDATE
        defaultCourseShouldBeFound("startdate.greaterOrEqualThan=" + DEFAULT_STARTDATE);

        // Get all the courseList where startdate greater than or equals to UPDATED_STARTDATE
        defaultCourseShouldNotBeFound("startdate.greaterOrEqualThan=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartdateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startdate less than or equals to DEFAULT_STARTDATE
        defaultCourseShouldNotBeFound("startdate.lessThan=" + DEFAULT_STARTDATE);

        // Get all the courseList where startdate less than or equals to UPDATED_STARTDATE
        defaultCourseShouldBeFound("startdate.lessThan=" + UPDATED_STARTDATE);
    }


    @Test
    @Transactional
    public void getAllCoursesByEnddateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where enddate equals to DEFAULT_ENDDATE
        defaultCourseShouldBeFound("enddate.equals=" + DEFAULT_ENDDATE);

        // Get all the courseList where enddate equals to UPDATED_ENDDATE
        defaultCourseShouldNotBeFound("enddate.equals=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEnddateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where enddate in DEFAULT_ENDDATE or UPDATED_ENDDATE
        defaultCourseShouldBeFound("enddate.in=" + DEFAULT_ENDDATE + "," + UPDATED_ENDDATE);

        // Get all the courseList where enddate equals to UPDATED_ENDDATE
        defaultCourseShouldNotBeFound("enddate.in=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEnddateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where enddate is not null
        defaultCourseShouldBeFound("enddate.specified=true");

        // Get all the courseList where enddate is null
        defaultCourseShouldNotBeFound("enddate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByEnddateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where enddate greater than or equals to DEFAULT_ENDDATE
        defaultCourseShouldBeFound("enddate.greaterOrEqualThan=" + DEFAULT_ENDDATE);

        // Get all the courseList where enddate greater than or equals to UPDATED_ENDDATE
        defaultCourseShouldNotBeFound("enddate.greaterOrEqualThan=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEnddateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where enddate less than or equals to DEFAULT_ENDDATE
        defaultCourseShouldNotBeFound("enddate.lessThan=" + DEFAULT_ENDDATE);

        // Get all the courseList where enddate less than or equals to UPDATED_ENDDATE
        defaultCourseShouldBeFound("enddate.lessThan=" + UPDATED_ENDDATE);
    }


    @Test
    @Transactional
    public void getAllCoursesByPointIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where point equals to DEFAULT_POINT
        defaultCourseShouldBeFound("point.equals=" + DEFAULT_POINT);

        // Get all the courseList where point equals to UPDATED_POINT
        defaultCourseShouldNotBeFound("point.equals=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllCoursesByPointIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where point in DEFAULT_POINT or UPDATED_POINT
        defaultCourseShouldBeFound("point.in=" + DEFAULT_POINT + "," + UPDATED_POINT);

        // Get all the courseList where point equals to UPDATED_POINT
        defaultCourseShouldNotBeFound("point.in=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllCoursesByPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where point is not null
        defaultCourseShouldBeFound("point.specified=true");

        // Get all the courseList where point is null
        defaultCourseShouldNotBeFound("point.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where point greater than or equals to DEFAULT_POINT
        defaultCourseShouldBeFound("point.greaterOrEqualThan=" + DEFAULT_POINT);

        // Get all the courseList where point greater than or equals to UPDATED_POINT
        defaultCourseShouldNotBeFound("point.greaterOrEqualThan=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllCoursesByPointIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where point less than or equals to DEFAULT_POINT
        defaultCourseShouldNotBeFound("point.lessThan=" + DEFAULT_POINT);

        // Get all the courseList where point less than or equals to UPDATED_POINT
        defaultCourseShouldBeFound("point.lessThan=" + UPDATED_POINT);
    }


    @Test
    @Transactional
    public void getAllCoursesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credit equals to DEFAULT_CREDIT
        defaultCourseShouldBeFound("credit.equals=" + DEFAULT_CREDIT);

        // Get all the courseList where credit equals to UPDATED_CREDIT
        defaultCourseShouldNotBeFound("credit.equals=" + UPDATED_CREDIT);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credit in DEFAULT_CREDIT or UPDATED_CREDIT
        defaultCourseShouldBeFound("credit.in=" + DEFAULT_CREDIT + "," + UPDATED_CREDIT);

        // Get all the courseList where credit equals to UPDATED_CREDIT
        defaultCourseShouldNotBeFound("credit.in=" + UPDATED_CREDIT);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credit is not null
        defaultCourseShouldBeFound("credit.specified=true");

        // Get all the courseList where credit is null
        defaultCourseShouldNotBeFound("credit.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where country equals to DEFAULT_COUNTRY
        defaultCourseShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the courseList where country equals to UPDATED_COUNTRY
        defaultCourseShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCoursesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCourseShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the courseList where country equals to UPDATED_COUNTRY
        defaultCourseShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCoursesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where country is not null
        defaultCourseShouldBeFound("country.specified=true");

        // Get all the courseList where country is null
        defaultCourseShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where state equals to DEFAULT_STATE
        defaultCourseShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the courseList where state equals to UPDATED_STATE
        defaultCourseShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where state in DEFAULT_STATE or UPDATED_STATE
        defaultCourseShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the courseList where state equals to UPDATED_STATE
        defaultCourseShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where state is not null
        defaultCourseShouldBeFound("state.specified=true");

        // Get all the courseList where state is null
        defaultCourseShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByTopicIsEqualToSomething() throws Exception {
        // Initialize the database
        Topic topic = TopicResourceIntTest.createEntity(em);
        em.persist(topic);
        em.flush();
        course.setTopic(topic);
        courseRepository.saveAndFlush(course);
        Long topicId = topic.getId();

        // Get all the courseList where topic equals to topicId
        defaultCourseShouldBeFound("topicId.equals=" + topicId);

        // Get all the courseList where topic equals to topicId + 1
        defaultCourseShouldNotBeFound("topicId.equals=" + (topicId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].normCourses").value(hasItem(DEFAULT_NORM_COURSES.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(sameInstant(DEFAULT_STARTDATE))))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(sameInstant(DEFAULT_ENDDATE))))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.intValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseService.save(course);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCourseSearchRepository);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .title(UPDATED_TITLE)
            .section(UPDATED_SECTION)
            .normCourses(UPDATED_NORM_COURSES)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .startdate(UPDATED_STARTDATE)
            .enddate(UPDATED_ENDDATE)
            .point(UPDATED_POINT)
            .credit(UPDATED_CREDIT)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testCourse.getNormCourses()).isEqualTo(UPDATED_NORM_COURSES);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCourse.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCourse.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCourse.getStartdate()).isEqualTo(UPDATED_STARTDATE);
        assertThat(testCourse.getEnddate()).isEqualTo(UPDATED_ENDDATE);
        assertThat(testCourse.getPoint()).isEqualTo(UPDATED_POINT);
        assertThat(testCourse.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testCourse.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCourse.getState()).isEqualTo(UPDATED_STATE);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(1)).save(testCourse);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(0)).save(course);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Get the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(1)).deleteById(course.getId());
    }

    @Test
    @Transactional
    public void searchCourse() throws Exception {
        // Initialize the database
        courseService.save(course);
        when(mockCourseSearchRepository.search(queryStringQuery("id:" + course.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(course), PageRequest.of(0, 1), 1));
        // Search the course
        restCourseMockMvc.perform(get("/api/_search/courses?query=id:" + course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].normCourses").value(hasItem(DEFAULT_NORM_COURSES.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(sameInstant(DEFAULT_STARTDATE))))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(sameInstant(DEFAULT_ENDDATE))))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT.intValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
