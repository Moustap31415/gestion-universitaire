package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.CurriculumAsserts.*;
import static sn.edu.ugb.curriculum.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.IntegrationTest;
import sn.edu.ugb.curriculum.domain.Curriculum;
import sn.edu.ugb.curriculum.repository.CurriculumRepository;
import sn.edu.ugb.curriculum.service.dto.CurriculumDTO;
import sn.edu.ugb.curriculum.service.mapper.CurriculumMapper;

/**
 * Integration tests for the {@link CurriculumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurriculumResourceIT {

    private static final Long DEFAULT_FILIERE_ID = 1L;
    private static final Long UPDATED_FILIERE_ID = 2L;

    private static final Long DEFAULT_MODULE_ID = 1L;
    private static final Long UPDATED_MODULE_ID = 2L;

    private static final Long DEFAULT_SEMESTRE_ID = 1L;
    private static final Long UPDATED_SEMESTRE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/curricula";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurriculumMockMvc;

    private Curriculum curriculum;

    private Curriculum insertedCurriculum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curriculum createEntity() {
        return new Curriculum().filiereId(DEFAULT_FILIERE_ID).moduleId(DEFAULT_MODULE_ID).semestreId(DEFAULT_SEMESTRE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curriculum createUpdatedEntity() {
        return new Curriculum().filiereId(UPDATED_FILIERE_ID).moduleId(UPDATED_MODULE_ID).semestreId(UPDATED_SEMESTRE_ID);
    }

    @BeforeEach
    void initTest() {
        curriculum = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCurriculum != null) {
            curriculumRepository.delete(insertedCurriculum);
            insertedCurriculum = null;
        }
    }

    @Test
    @Transactional
    void createCurriculum() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);
        var returnedCurriculumDTO = om.readValue(
            restCurriculumMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curriculumDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CurriculumDTO.class
        );

        // Validate the Curriculum in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCurriculum = curriculumMapper.toEntity(returnedCurriculumDTO);
        assertCurriculumUpdatableFieldsEquals(returnedCurriculum, getPersistedCurriculum(returnedCurriculum));

        insertedCurriculum = returnedCurriculum;
    }

    @Test
    @Transactional
    void createCurriculumWithExistingId() throws Exception {
        // Create the Curriculum with an existing ID
        curriculum.setId(1L);
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurriculumMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curriculumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFiliereIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        curriculum.setFiliereId(null);

        // Create the Curriculum, which fails.
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        restCurriculumMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curriculumDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModuleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        curriculum.setModuleId(null);

        // Create the Curriculum, which fails.
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        restCurriculumMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curriculumDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSemestreIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        curriculum.setSemestreId(null);

        // Create the Curriculum, which fails.
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        restCurriculumMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curriculumDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurricula() throws Exception {
        // Initialize the database
        insertedCurriculum = curriculumRepository.saveAndFlush(curriculum);

        // Get all the curriculumList
        restCurriculumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curriculum.getId().intValue())))
            .andExpect(jsonPath("$.[*].filiereId").value(hasItem(DEFAULT_FILIERE_ID.intValue())))
            .andExpect(jsonPath("$.[*].moduleId").value(hasItem(DEFAULT_MODULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].semestreId").value(hasItem(DEFAULT_SEMESTRE_ID.intValue())));
    }

    @Test
    @Transactional
    void getCurriculum() throws Exception {
        // Initialize the database
        insertedCurriculum = curriculumRepository.saveAndFlush(curriculum);

        // Get the curriculum
        restCurriculumMockMvc
            .perform(get(ENTITY_API_URL_ID, curriculum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(curriculum.getId().intValue()))
            .andExpect(jsonPath("$.filiereId").value(DEFAULT_FILIERE_ID.intValue()))
            .andExpect(jsonPath("$.moduleId").value(DEFAULT_MODULE_ID.intValue()))
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCurriculum() throws Exception {
        // Get the curriculum
        restCurriculumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurriculum() throws Exception {
        // Initialize the database
        insertedCurriculum = curriculumRepository.saveAndFlush(curriculum);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curriculum
        Curriculum updatedCurriculum = curriculumRepository.findById(curriculum.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCurriculum are not directly saved in db
        em.detach(updatedCurriculum);
        updatedCurriculum.filiereId(UPDATED_FILIERE_ID).moduleId(UPDATED_MODULE_ID).semestreId(UPDATED_SEMESTRE_ID);
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(updatedCurriculum);

        restCurriculumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, curriculumDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(curriculumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCurriculumToMatchAllProperties(updatedCurriculum);
    }

    @Test
    @Transactional
    void putNonExistingCurriculum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curriculum.setId(longCount.incrementAndGet());

        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, curriculumDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(curriculumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurriculum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curriculum.setId(longCount.incrementAndGet());

        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(curriculumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurriculum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curriculum.setId(longCount.incrementAndGet());

        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(curriculumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurriculumWithPatch() throws Exception {
        // Initialize the database
        insertedCurriculum = curriculumRepository.saveAndFlush(curriculum);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curriculum using partial update
        Curriculum partialUpdatedCurriculum = new Curriculum();
        partialUpdatedCurriculum.setId(curriculum.getId());

        partialUpdatedCurriculum.filiereId(UPDATED_FILIERE_ID).moduleId(UPDATED_MODULE_ID).semestreId(UPDATED_SEMESTRE_ID);

        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurriculum.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurriculum))
            )
            .andExpect(status().isOk());

        // Validate the Curriculum in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCurriculumUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCurriculum, curriculum),
            getPersistedCurriculum(curriculum)
        );
    }

    @Test
    @Transactional
    void fullUpdateCurriculumWithPatch() throws Exception {
        // Initialize the database
        insertedCurriculum = curriculumRepository.saveAndFlush(curriculum);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curriculum using partial update
        Curriculum partialUpdatedCurriculum = new Curriculum();
        partialUpdatedCurriculum.setId(curriculum.getId());

        partialUpdatedCurriculum.filiereId(UPDATED_FILIERE_ID).moduleId(UPDATED_MODULE_ID).semestreId(UPDATED_SEMESTRE_ID);

        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurriculum.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurriculum))
            )
            .andExpect(status().isOk());

        // Validate the Curriculum in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCurriculumUpdatableFieldsEquals(partialUpdatedCurriculum, getPersistedCurriculum(partialUpdatedCurriculum));
    }

    @Test
    @Transactional
    void patchNonExistingCurriculum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curriculum.setId(longCount.incrementAndGet());

        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, curriculumDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(curriculumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurriculum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curriculum.setId(longCount.incrementAndGet());

        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(curriculumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurriculum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curriculum.setId(longCount.incrementAndGet());

        // Create the Curriculum
        CurriculumDTO curriculumDTO = curriculumMapper.toDto(curriculum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(curriculumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curriculum in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurriculum() throws Exception {
        // Initialize the database
        insertedCurriculum = curriculumRepository.saveAndFlush(curriculum);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the curriculum
        restCurriculumMockMvc
            .perform(delete(ENTITY_API_URL_ID, curriculum.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return curriculumRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Curriculum getPersistedCurriculum(Curriculum curriculum) {
        return curriculumRepository.findById(curriculum.getId()).orElseThrow();
    }

    protected void assertPersistedCurriculumToMatchAllProperties(Curriculum expectedCurriculum) {
        assertCurriculumAllPropertiesEquals(expectedCurriculum, getPersistedCurriculum(expectedCurriculum));
    }

    protected void assertPersistedCurriculumToMatchUpdatableProperties(Curriculum expectedCurriculum) {
        assertCurriculumAllUpdatablePropertiesEquals(expectedCurriculum, getPersistedCurriculum(expectedCurriculum));
    }
}
