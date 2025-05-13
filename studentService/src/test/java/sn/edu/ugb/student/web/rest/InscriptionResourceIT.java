package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.student.domain.InscriptionAsserts.*;
import static sn.edu.ugb.student.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.student.IntegrationTest;
import sn.edu.ugb.student.domain.Inscription;
import sn.edu.ugb.student.repository.InscriptionRepository;
import sn.edu.ugb.student.service.dto.InscriptionDTO;
import sn.edu.ugb.student.service.mapper.InscriptionMapper;

/**
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionResourceIT {

    private static final Boolean DEFAULT_EN_COURS = false;
    private static final Boolean UPDATED_EN_COURS = true;

    private static final Long DEFAULT_ETUDIANT_ID = 1L;
    private static final Long UPDATED_ETUDIANT_ID = 2L;

    private static final Long DEFAULT_FILIERE_ID = 1L;
    private static final Long UPDATED_FILIERE_ID = 2L;

    private static final Long DEFAULT_SEMESTRE_ID = 1L;
    private static final Long UPDATED_SEMESTRE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    private Inscription insertedInscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity() {
        return new Inscription()
            .enCours(DEFAULT_EN_COURS)
            .etudiantId(DEFAULT_ETUDIANT_ID)
            .filiereId(DEFAULT_FILIERE_ID)
            .semestreId(DEFAULT_SEMESTRE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity() {
        return new Inscription()
            .enCours(UPDATED_EN_COURS)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .filiereId(UPDATED_FILIERE_ID)
            .semestreId(UPDATED_SEMESTRE_ID);
    }

    @BeforeEach
    void initTest() {
        inscription = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInscription != null) {
            inscriptionRepository.delete(insertedInscription);
            insertedInscription = null;
        }
    }

    @Test
    @Transactional
    void createInscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);
        var returnedInscriptionDTO = om.readValue(
            restInscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InscriptionDTO.class
        );

        // Validate the Inscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInscription = inscriptionMapper.toEntity(returnedInscriptionDTO);
        assertInscriptionUpdatableFieldsEquals(returnedInscription, getPersistedInscription(returnedInscription));

        insertedInscription = returnedInscription;
    }

    @Test
    @Transactional
    void createInscriptionWithExistingId() throws Exception {
        // Create the Inscription with an existing ID
        inscription.setId(1L);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnCoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscription.setEnCours(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtudiantIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscription.setEtudiantId(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiliereIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscription.setFiliereId(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSemestreIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscription.setSemestreId(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInscriptions() throws Exception {
        // Initialize the database
        insertedInscription = inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].enCours").value(hasItem(DEFAULT_EN_COURS)))
            .andExpect(jsonPath("$.[*].etudiantId").value(hasItem(DEFAULT_ETUDIANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].filiereId").value(hasItem(DEFAULT_FILIERE_ID.intValue())))
            .andExpect(jsonPath("$.[*].semestreId").value(hasItem(DEFAULT_SEMESTRE_ID.intValue())));
    }

    @Test
    @Transactional
    void getInscription() throws Exception {
        // Initialize the database
        insertedInscription = inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.enCours").value(DEFAULT_EN_COURS))
            .andExpect(jsonPath("$.etudiantId").value(DEFAULT_ETUDIANT_ID.intValue()))
            .andExpect(jsonPath("$.filiereId").value(DEFAULT_FILIERE_ID.intValue()))
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscription() throws Exception {
        // Initialize the database
        insertedInscription = inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription
            .enCours(UPDATED_EN_COURS)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .filiereId(UPDATED_FILIERE_ID)
            .semestreId(UPDATED_SEMESTRE_ID);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(updatedInscription);

        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInscriptionToMatchAllProperties(updatedInscription);
    }

    @Test
    @Transactional
    void putNonExistingInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedInscription = inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription.filiereId(UPDATED_FILIERE_ID);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInscription, inscription),
            getPersistedInscription(inscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedInscription = inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .enCours(UPDATED_EN_COURS)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .filiereId(UPDATED_FILIERE_ID)
            .semestreId(UPDATED_SEMESTRE_ID);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscriptionUpdatableFieldsEquals(partialUpdatedInscription, getPersistedInscription(partialUpdatedInscription));
    }

    @Test
    @Transactional
    void patchNonExistingInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscription.setId(longCount.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscription() throws Exception {
        // Initialize the database
        insertedInscription = inscriptionRepository.saveAndFlush(inscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inscription
        restInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscription.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inscriptionRepository.count();
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

    protected Inscription getPersistedInscription(Inscription inscription) {
        return inscriptionRepository.findById(inscription.getId()).orElseThrow();
    }

    protected void assertPersistedInscriptionToMatchAllProperties(Inscription expectedInscription) {
        assertInscriptionAllPropertiesEquals(expectedInscription, getPersistedInscription(expectedInscription));
    }

    protected void assertPersistedInscriptionToMatchUpdatableProperties(Inscription expectedInscription) {
        assertInscriptionAllUpdatablePropertiesEquals(expectedInscription, getPersistedInscription(expectedInscription));
    }
}
