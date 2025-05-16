package sn.edu.ugb.grade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.grade.domain.EvaluationAsserts.*;
import static sn.edu.ugb.grade.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.edu.ugb.grade.IntegrationTest;
import sn.edu.ugb.grade.domain.Evaluation;
import sn.edu.ugb.grade.domain.enumeration.TypeEvaluation;
import sn.edu.ugb.grade.repository.EvaluationRepository;
import sn.edu.ugb.grade.service.dto.EvaluationDTO;
import sn.edu.ugb.grade.service.mapper.EvaluationMapper;

/**
 * Integration tests for the {@link EvaluationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvaluationResourceIT {

    private static final TypeEvaluation DEFAULT_TYPE = TypeEvaluation.DS;
    private static final TypeEvaluation UPDATED_TYPE = TypeEvaluation.EXAMEN;

    private static final Float DEFAULT_NOTE_MAXIMALE = 1F;
    private static final Float UPDATED_NOTE_MAXIMALE = 2F;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_COURS_ID = 1L;
    private static final Long UPDATED_COURS_ID = 2L;

    private static final Long DEFAULT_SESSION_ID = 1L;
    private static final Long UPDATED_SESSION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/evaluations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluationMockMvc;

    private Evaluation evaluation;

    private Evaluation insertedEvaluation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createEntity() {
        return new Evaluation()
            .type(DEFAULT_TYPE)
            .noteMaximale(DEFAULT_NOTE_MAXIMALE)
            .date(DEFAULT_DATE)
            .coursId(DEFAULT_COURS_ID)
            .sessionId(DEFAULT_SESSION_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createUpdatedEntity() {
        return new Evaluation()
            .type(UPDATED_TYPE)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .date(UPDATED_DATE)
            .coursId(UPDATED_COURS_ID)
            .sessionId(UPDATED_SESSION_ID);
    }

    @BeforeEach
    public void initTest() {
        evaluation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEvaluation != null) {
            evaluationRepository.delete(insertedEvaluation);
            insertedEvaluation = null;
        }
    }

    @Test
    @Transactional
    void createEvaluation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);
        var returnedEvaluationDTO = om.readValue(
            restEvaluationMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EvaluationDTO.class
        );

        // Validate the Evaluation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvaluation = evaluationMapper.toEntity(returnedEvaluationDTO);
        assertEvaluationUpdatableFieldsEquals(returnedEvaluation, getPersistedEvaluation(returnedEvaluation));

        insertedEvaluation = returnedEvaluation;
    }

    @Test
    @Transactional
    void createEvaluationWithExistingId() throws Exception {
        // Create the Evaluation with an existing ID
        evaluation.setId(1L);
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setType(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteMaximaleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setNoteMaximale(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setDate(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoursIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setCoursId(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluation.setSessionId(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluations() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].noteMaximale").value(hasItem(DEFAULT_NOTE_MAXIMALE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].coursId").value(hasItem(DEFAULT_COURS_ID.intValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.intValue())));
    }

    @Test
    @Transactional
    void getEvaluation() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        // Get the evaluation
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.noteMaximale").value(DEFAULT_NOTE_MAXIMALE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.coursId").value(DEFAULT_COURS_ID.intValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEvaluation() throws Exception {
        // Get the evaluation
        restEvaluationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvaluation() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluation
        Evaluation updatedEvaluation = evaluationRepository.findById(evaluation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvaluation are not directly saved in db
        em.detach(updatedEvaluation);
        updatedEvaluation
            .type(UPDATED_TYPE)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .date(UPDATED_DATE)
            .coursId(UPDATED_COURS_ID)
            .sessionId(UPDATED_SESSION_ID);
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(updatedEvaluation);

        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEvaluationToMatchAllProperties(updatedEvaluation);
    }

    @Test
    @Transactional
    void putNonExistingEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluation using partial update
        Evaluation partialUpdatedEvaluation = new Evaluation();
        partialUpdatedEvaluation.setId(evaluation.getId());

        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEvaluation, evaluation),
            getPersistedEvaluation(evaluation)
        );
    }

    @Test
    @Transactional
    void fullUpdateEvaluationWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluation using partial update
        Evaluation partialUpdatedEvaluation = new Evaluation();
        partialUpdatedEvaluation.setId(evaluation.getId());

        partialUpdatedEvaluation
            .type(UPDATED_TYPE)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .date(UPDATED_DATE)
            .coursId(UPDATED_COURS_ID)
            .sessionId(UPDATED_SESSION_ID);

        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationUpdatableFieldsEquals(partialUpdatedEvaluation, getPersistedEvaluation(partialUpdatedEvaluation));
    }

    @Test
    @Transactional
    void patchNonExistingEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluation.setId(longCount.incrementAndGet());

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(evaluationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluation() throws Exception {
        // Initialize the database
        insertedEvaluation = evaluationRepository.saveAndFlush(evaluation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evaluation
        restEvaluationMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return evaluationRepository.count();
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

    protected Evaluation getPersistedEvaluation(Evaluation evaluation) {
        return evaluationRepository.findById(evaluation.getId()).orElseThrow();
    }

    protected void assertPersistedEvaluationToMatchAllProperties(Evaluation expectedEvaluation) {
        assertEvaluationAllPropertiesEquals(expectedEvaluation, getPersistedEvaluation(expectedEvaluation));
    }

    protected void assertPersistedEvaluationToMatchUpdatableProperties(Evaluation expectedEvaluation) {
        assertEvaluationAllUpdatablePropertiesEquals(expectedEvaluation, getPersistedEvaluation(expectedEvaluation));
    }
}
