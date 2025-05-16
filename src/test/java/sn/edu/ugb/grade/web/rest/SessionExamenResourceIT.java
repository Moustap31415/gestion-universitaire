package sn.edu.ugb.grade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.grade.domain.SessionExamenAsserts.*;
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
import sn.edu.ugb.grade.domain.SessionExamen;
import sn.edu.ugb.grade.repository.SessionExamenRepository;
import sn.edu.ugb.grade.service.dto.SessionExamenDTO;
import sn.edu.ugb.grade.service.mapper.SessionExamenMapper;

/**
 * Integration tests for the {@link SessionExamenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionExamenResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/session-examen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SessionExamenRepository sessionExamenRepository;

    @Autowired
    private SessionExamenMapper sessionExamenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionExamenMockMvc;

    private SessionExamen sessionExamen;

    private SessionExamen insertedSessionExamen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionExamen createEntity() {
        return new SessionExamen().nom(DEFAULT_NOM).dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionExamen createUpdatedEntity() {
        return new SessionExamen().nom(UPDATED_NOM).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
    }

    @BeforeEach
    public void initTest() {
        sessionExamen = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSessionExamen != null) {
            sessionExamenRepository.delete(insertedSessionExamen);
            insertedSessionExamen = null;
        }
    }

    @Test
    @Transactional
    void createSessionExamen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);
        var returnedSessionExamenDTO = om.readValue(
            restSessionExamenMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(sessionExamenDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SessionExamenDTO.class
        );

        // Validate the SessionExamen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSessionExamen = sessionExamenMapper.toEntity(returnedSessionExamenDTO);
        assertSessionExamenUpdatableFieldsEquals(returnedSessionExamen, getPersistedSessionExamen(returnedSessionExamen));

        insertedSessionExamen = returnedSessionExamen;
    }

    @Test
    @Transactional
    void createSessionExamenWithExistingId() throws Exception {
        // Create the SessionExamen with an existing ID
        sessionExamen.setId(1L);
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionExamenMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sessionExamen.setNom(null);

        // Create the SessionExamen, which fails.
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        restSessionExamenMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sessionExamen.setDateDebut(null);

        // Create the SessionExamen, which fails.
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        restSessionExamenMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sessionExamen.setDateFin(null);

        // Create the SessionExamen, which fails.
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        restSessionExamenMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSessionExamen() throws Exception {
        // Initialize the database
        insertedSessionExamen = sessionExamenRepository.saveAndFlush(sessionExamen);

        // Get all the sessionExamenList
        restSessionExamenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionExamen.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getSessionExamen() throws Exception {
        // Initialize the database
        insertedSessionExamen = sessionExamenRepository.saveAndFlush(sessionExamen);

        // Get the sessionExamen
        restSessionExamenMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionExamen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionExamen.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSessionExamen() throws Exception {
        // Get the sessionExamen
        restSessionExamenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionExamen() throws Exception {
        // Initialize the database
        insertedSessionExamen = sessionExamenRepository.saveAndFlush(sessionExamen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionExamen
        SessionExamen updatedSessionExamen = sessionExamenRepository.findById(sessionExamen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSessionExamen are not directly saved in db
        em.detach(updatedSessionExamen);
        updatedSessionExamen.nom(UPDATED_NOM).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(updatedSessionExamen);

        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionExamenDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSessionExamenToMatchAllProperties(updatedSessionExamen);
    }

    @Test
    @Transactional
    void putNonExistingSessionExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionExamen.setId(longCount.incrementAndGet());

        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionExamenDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionExamen.setId(longCount.incrementAndGet());

        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionExamen.setId(longCount.incrementAndGet());

        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionExamenWithPatch() throws Exception {
        // Initialize the database
        insertedSessionExamen = sessionExamenRepository.saveAndFlush(sessionExamen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionExamen using partial update
        SessionExamen partialUpdatedSessionExamen = new SessionExamen();
        partialUpdatedSessionExamen.setId(sessionExamen.getId());

        partialUpdatedSessionExamen.nom(UPDATED_NOM).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionExamen.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSessionExamen))
            )
            .andExpect(status().isOk());

        // Validate the SessionExamen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionExamenUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSessionExamen, sessionExamen),
            getPersistedSessionExamen(sessionExamen)
        );
    }

    @Test
    @Transactional
    void fullUpdateSessionExamenWithPatch() throws Exception {
        // Initialize the database
        insertedSessionExamen = sessionExamenRepository.saveAndFlush(sessionExamen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionExamen using partial update
        SessionExamen partialUpdatedSessionExamen = new SessionExamen();
        partialUpdatedSessionExamen.setId(sessionExamen.getId());

        partialUpdatedSessionExamen.nom(UPDATED_NOM).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionExamen.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSessionExamen))
            )
            .andExpect(status().isOk());

        // Validate the SessionExamen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionExamenUpdatableFieldsEquals(partialUpdatedSessionExamen, getPersistedSessionExamen(partialUpdatedSessionExamen));
    }

    @Test
    @Transactional
    void patchNonExistingSessionExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionExamen.setId(longCount.incrementAndGet());

        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionExamenDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionExamen.setId(longCount.incrementAndGet());

        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionExamen.setId(longCount.incrementAndGet());

        // Create the SessionExamen
        SessionExamenDTO sessionExamenDTO = sessionExamenMapper.toDto(sessionExamen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionExamenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionExamen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionExamen() throws Exception {
        // Initialize the database
        insertedSessionExamen = sessionExamenRepository.saveAndFlush(sessionExamen);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sessionExamen
        restSessionExamenMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionExamen.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sessionExamenRepository.count();
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

    protected SessionExamen getPersistedSessionExamen(SessionExamen sessionExamen) {
        return sessionExamenRepository.findById(sessionExamen.getId()).orElseThrow();
    }

    protected void assertPersistedSessionExamenToMatchAllProperties(SessionExamen expectedSessionExamen) {
        assertSessionExamenAllPropertiesEquals(expectedSessionExamen, getPersistedSessionExamen(expectedSessionExamen));
    }

    protected void assertPersistedSessionExamenToMatchUpdatableProperties(SessionExamen expectedSessionExamen) {
        assertSessionExamenAllUpdatablePropertiesEquals(expectedSessionExamen, getPersistedSessionExamen(expectedSessionExamen));
    }
}
