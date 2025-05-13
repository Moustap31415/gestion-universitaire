package sn.edu.ugb.teacher.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.teacher.domain.AffectationEnseignementAsserts.*;
import static sn.edu.ugb.teacher.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.teacher.IntegrationTest;
import sn.edu.ugb.teacher.domain.AffectationEnseignement;
import sn.edu.ugb.teacher.repository.AffectationEnseignementRepository;
import sn.edu.ugb.teacher.service.dto.AffectationEnseignementDTO;
import sn.edu.ugb.teacher.service.mapper.AffectationEnseignementMapper;

/**
 * Integration tests for the {@link AffectationEnseignementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffectationEnseignementResourceIT {

    private static final String DEFAULT_ANNEE_ACADEMIQUE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_ACADEMIQUE = "BBBBBBBBBB";

    private static final Long DEFAULT_ENSEIGNANT_ID = 1L;
    private static final Long UPDATED_ENSEIGNANT_ID = 2L;

    private static final Long DEFAULT_COURS_ID = 1L;
    private static final Long UPDATED_COURS_ID = 2L;

    private static final String ENTITY_API_URL = "/api/affectation-enseignements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AffectationEnseignementRepository affectationEnseignementRepository;

    @Autowired
    private AffectationEnseignementMapper affectationEnseignementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffectationEnseignementMockMvc;

    private AffectationEnseignement affectationEnseignement;

    private AffectationEnseignement insertedAffectationEnseignement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationEnseignement createEntity() {
        return new AffectationEnseignement()
            .anneeAcademique(DEFAULT_ANNEE_ACADEMIQUE)
            .enseignantId(DEFAULT_ENSEIGNANT_ID)
            .coursId(DEFAULT_COURS_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationEnseignement createUpdatedEntity() {
        return new AffectationEnseignement()
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .enseignantId(UPDATED_ENSEIGNANT_ID)
            .coursId(UPDATED_COURS_ID);
    }

    @BeforeEach
    void initTest() {
        affectationEnseignement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAffectationEnseignement != null) {
            affectationEnseignementRepository.delete(insertedAffectationEnseignement);
            insertedAffectationEnseignement = null;
        }
    }

    @Test
    @Transactional
    void createAffectationEnseignement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);
        var returnedAffectationEnseignementDTO = om.readValue(
            restAffectationEnseignementMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(affectationEnseignementDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AffectationEnseignementDTO.class
        );

        // Validate the AffectationEnseignement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAffectationEnseignement = affectationEnseignementMapper.toEntity(returnedAffectationEnseignementDTO);
        assertAffectationEnseignementUpdatableFieldsEquals(
            returnedAffectationEnseignement,
            getPersistedAffectationEnseignement(returnedAffectationEnseignement)
        );

        insertedAffectationEnseignement = returnedAffectationEnseignement;
    }

    @Test
    @Transactional
    void createAffectationEnseignementWithExistingId() throws Exception {
        // Create the AffectationEnseignement with an existing ID
        affectationEnseignement.setId(1L);
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnneeAcademiqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        affectationEnseignement.setAnneeAcademique(null);

        // Create the AffectationEnseignement, which fails.
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        restAffectationEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnseignantIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        affectationEnseignement.setEnseignantId(null);

        // Create the AffectationEnseignement, which fails.
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        restAffectationEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoursIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        affectationEnseignement.setCoursId(null);

        // Create the AffectationEnseignement, which fails.
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        restAffectationEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffectationEnseignements() throws Exception {
        // Initialize the database
        insertedAffectationEnseignement = affectationEnseignementRepository.saveAndFlush(affectationEnseignement);

        // Get all the affectationEnseignementList
        restAffectationEnseignementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectationEnseignement.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeAcademique").value(hasItem(DEFAULT_ANNEE_ACADEMIQUE)))
            .andExpect(jsonPath("$.[*].enseignantId").value(hasItem(DEFAULT_ENSEIGNANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].coursId").value(hasItem(DEFAULT_COURS_ID.intValue())));
    }

    @Test
    @Transactional
    void getAffectationEnseignement() throws Exception {
        // Initialize the database
        insertedAffectationEnseignement = affectationEnseignementRepository.saveAndFlush(affectationEnseignement);

        // Get the affectationEnseignement
        restAffectationEnseignementMockMvc
            .perform(get(ENTITY_API_URL_ID, affectationEnseignement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affectationEnseignement.getId().intValue()))
            .andExpect(jsonPath("$.anneeAcademique").value(DEFAULT_ANNEE_ACADEMIQUE))
            .andExpect(jsonPath("$.enseignantId").value(DEFAULT_ENSEIGNANT_ID.intValue()))
            .andExpect(jsonPath("$.coursId").value(DEFAULT_COURS_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAffectationEnseignement() throws Exception {
        // Get the affectationEnseignement
        restAffectationEnseignementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAffectationEnseignement() throws Exception {
        // Initialize the database
        insertedAffectationEnseignement = affectationEnseignementRepository.saveAndFlush(affectationEnseignement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationEnseignement
        AffectationEnseignement updatedAffectationEnseignement = affectationEnseignementRepository
            .findById(affectationEnseignement.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAffectationEnseignement are not directly saved in db
        em.detach(updatedAffectationEnseignement);
        updatedAffectationEnseignement
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .enseignantId(UPDATED_ENSEIGNANT_ID)
            .coursId(UPDATED_COURS_ID);
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(updatedAffectationEnseignement);

        restAffectationEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationEnseignementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isOk());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAffectationEnseignementToMatchAllProperties(updatedAffectationEnseignement);
    }

    @Test
    @Transactional
    void putNonExistingAffectationEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationEnseignement.setId(longCount.incrementAndGet());

        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationEnseignementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffectationEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationEnseignement.setId(longCount.incrementAndGet());

        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffectationEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationEnseignement.setId(longCount.incrementAndGet());

        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffectationEnseignementWithPatch() throws Exception {
        // Initialize the database
        insertedAffectationEnseignement = affectationEnseignementRepository.saveAndFlush(affectationEnseignement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationEnseignement using partial update
        AffectationEnseignement partialUpdatedAffectationEnseignement = new AffectationEnseignement();
        partialUpdatedAffectationEnseignement.setId(affectationEnseignement.getId());

        partialUpdatedAffectationEnseignement.enseignantId(UPDATED_ENSEIGNANT_ID).coursId(UPDATED_COURS_ID);

        restAffectationEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationEnseignement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAffectationEnseignement))
            )
            .andExpect(status().isOk());

        // Validate the AffectationEnseignement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAffectationEnseignementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAffectationEnseignement, affectationEnseignement),
            getPersistedAffectationEnseignement(affectationEnseignement)
        );
    }

    @Test
    @Transactional
    void fullUpdateAffectationEnseignementWithPatch() throws Exception {
        // Initialize the database
        insertedAffectationEnseignement = affectationEnseignementRepository.saveAndFlush(affectationEnseignement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationEnseignement using partial update
        AffectationEnseignement partialUpdatedAffectationEnseignement = new AffectationEnseignement();
        partialUpdatedAffectationEnseignement.setId(affectationEnseignement.getId());

        partialUpdatedAffectationEnseignement
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .enseignantId(UPDATED_ENSEIGNANT_ID)
            .coursId(UPDATED_COURS_ID);

        restAffectationEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationEnseignement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAffectationEnseignement))
            )
            .andExpect(status().isOk());

        // Validate the AffectationEnseignement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAffectationEnseignementUpdatableFieldsEquals(
            partialUpdatedAffectationEnseignement,
            getPersistedAffectationEnseignement(partialUpdatedAffectationEnseignement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAffectationEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationEnseignement.setId(longCount.incrementAndGet());

        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affectationEnseignementDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffectationEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationEnseignement.setId(longCount.incrementAndGet());

        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffectationEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationEnseignement.setId(longCount.incrementAndGet());

        // Create the AffectationEnseignement
        AffectationEnseignementDTO affectationEnseignementDTO = affectationEnseignementMapper.toDto(affectationEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationEnseignementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffectationEnseignement() throws Exception {
        // Initialize the database
        insertedAffectationEnseignement = affectationEnseignementRepository.saveAndFlush(affectationEnseignement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the affectationEnseignement
        restAffectationEnseignementMockMvc
            .perform(delete(ENTITY_API_URL_ID, affectationEnseignement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return affectationEnseignementRepository.count();
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

    protected AffectationEnseignement getPersistedAffectationEnseignement(AffectationEnseignement affectationEnseignement) {
        return affectationEnseignementRepository.findById(affectationEnseignement.getId()).orElseThrow();
    }

    protected void assertPersistedAffectationEnseignementToMatchAllProperties(AffectationEnseignement expectedAffectationEnseignement) {
        assertAffectationEnseignementAllPropertiesEquals(
            expectedAffectationEnseignement,
            getPersistedAffectationEnseignement(expectedAffectationEnseignement)
        );
    }

    protected void assertPersistedAffectationEnseignementToMatchUpdatableProperties(
        AffectationEnseignement expectedAffectationEnseignement
    ) {
        assertAffectationEnseignementAllUpdatablePropertiesEquals(
            expectedAffectationEnseignement,
            getPersistedAffectationEnseignement(expectedAffectationEnseignement)
        );
    }
}
