package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.student.domain.HistoriqueAcademiqueAsserts.*;
import static sn.edu.ugb.student.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.edu.ugb.student.domain.HistoriqueAcademique;
import sn.edu.ugb.student.domain.enumeration.StatutAcademique;
import sn.edu.ugb.student.repository.HistoriqueAcademiqueRepository;
import sn.edu.ugb.student.service.dto.HistoriqueAcademiqueDTO;
import sn.edu.ugb.student.service.mapper.HistoriqueAcademiqueMapper;

/**
 * Integration tests for the {@link HistoriqueAcademiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueAcademiqueResourceIT {

    private static final StatutAcademique DEFAULT_STATUT = StatutAcademique.EN_COURS;
    private static final StatutAcademique UPDATED_STATUT = StatutAcademique.VALIDE;

    private static final Instant DEFAULT_DATE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ETUDIANT_ID = 1L;
    private static final Long UPDATED_ETUDIANT_ID = 2L;

    private static final Long DEFAULT_SEMESTRE_ID = 1L;
    private static final Long UPDATED_SEMESTRE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/historique-academiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueAcademiqueRepository historiqueAcademiqueRepository;

    @Autowired
    private HistoriqueAcademiqueMapper historiqueAcademiqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueAcademiqueMockMvc;

    private HistoriqueAcademique historiqueAcademique;

    private HistoriqueAcademique insertedHistoriqueAcademique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueAcademique createEntity() {
        return new HistoriqueAcademique()
            .statut(DEFAULT_STATUT)
            .dateInscription(DEFAULT_DATE_INSCRIPTION)
            .etudiantId(DEFAULT_ETUDIANT_ID)
            .semestreId(DEFAULT_SEMESTRE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueAcademique createUpdatedEntity() {
        return new HistoriqueAcademique()
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .semestreId(UPDATED_SEMESTRE_ID);
    }

    @BeforeEach
    void initTest() {
        historiqueAcademique = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriqueAcademique != null) {
            historiqueAcademiqueRepository.delete(insertedHistoriqueAcademique);
            insertedHistoriqueAcademique = null;
        }
    }

    @Test
    @Transactional
    void createHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);
        var returnedHistoriqueAcademiqueDTO = om.readValue(
            restHistoriqueAcademiqueMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriqueAcademiqueDTO.class
        );

        // Validate the HistoriqueAcademique in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriqueAcademique = historiqueAcademiqueMapper.toEntity(returnedHistoriqueAcademiqueDTO);
        assertHistoriqueAcademiqueUpdatableFieldsEquals(
            returnedHistoriqueAcademique,
            getPersistedHistoriqueAcademique(returnedHistoriqueAcademique)
        );

        insertedHistoriqueAcademique = returnedHistoriqueAcademique;
    }

    @Test
    @Transactional
    void createHistoriqueAcademiqueWithExistingId() throws Exception {
        // Create the HistoriqueAcademique with an existing ID
        historiqueAcademique.setId(1L);
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAcademique.setStatut(null);

        // Create the HistoriqueAcademique, which fails.
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateInscriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAcademique.setDateInscription(null);

        // Create the HistoriqueAcademique, which fails.
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtudiantIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAcademique.setEtudiantId(null);

        // Create the HistoriqueAcademique, which fails.
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSemestreIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAcademique.setSemestreId(null);

        // Create the HistoriqueAcademique, which fails.
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueAcademiques() throws Exception {
        // Initialize the database
        insertedHistoriqueAcademique = historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        // Get all the historiqueAcademiqueList
        restHistoriqueAcademiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueAcademique.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].etudiantId").value(hasItem(DEFAULT_ETUDIANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].semestreId").value(hasItem(DEFAULT_SEMESTRE_ID.intValue())));
    }

    @Test
    @Transactional
    void getHistoriqueAcademique() throws Exception {
        // Initialize the database
        insertedHistoriqueAcademique = historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        // Get the historiqueAcademique
        restHistoriqueAcademiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueAcademique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueAcademique.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.etudiantId").value(DEFAULT_ETUDIANT_ID.intValue()))
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueAcademique() throws Exception {
        // Get the historiqueAcademique
        restHistoriqueAcademiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriqueAcademique() throws Exception {
        // Initialize the database
        insertedHistoriqueAcademique = historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAcademique
        HistoriqueAcademique updatedHistoriqueAcademique = historiqueAcademiqueRepository
            .findById(historiqueAcademique.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriqueAcademique are not directly saved in db
        em.detach(updatedHistoriqueAcademique);
        updatedHistoriqueAcademique
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .semestreId(UPDATED_SEMESTRE_ID);
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(updatedHistoriqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueAcademiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriqueAcademiqueToMatchAllProperties(updatedHistoriqueAcademique);
    }

    @Test
    @Transactional
    void putNonExistingHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAcademique.setId(longCount.incrementAndGet());

        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueAcademiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAcademique.setId(longCount.incrementAndGet());

        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAcademique.setId(longCount.incrementAndGet());

        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueAcademiqueWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueAcademique = historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAcademique using partial update
        HistoriqueAcademique partialUpdatedHistoriqueAcademique = new HistoriqueAcademique();
        partialUpdatedHistoriqueAcademique.setId(historiqueAcademique.getId());

        partialUpdatedHistoriqueAcademique
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .semestreId(UPDATED_SEMESTRE_ID);

        restHistoriqueAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAcademique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueAcademique))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAcademique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueAcademiqueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriqueAcademique, historiqueAcademique),
            getPersistedHistoriqueAcademique(historiqueAcademique)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueAcademiqueWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueAcademique = historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAcademique using partial update
        HistoriqueAcademique partialUpdatedHistoriqueAcademique = new HistoriqueAcademique();
        partialUpdatedHistoriqueAcademique.setId(historiqueAcademique.getId());

        partialUpdatedHistoriqueAcademique
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .etudiantId(UPDATED_ETUDIANT_ID)
            .semestreId(UPDATED_SEMESTRE_ID);

        restHistoriqueAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAcademique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueAcademique))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAcademique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueAcademiqueUpdatableFieldsEquals(
            partialUpdatedHistoriqueAcademique,
            getPersistedHistoriqueAcademique(partialUpdatedHistoriqueAcademique)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAcademique.setId(longCount.incrementAndGet());

        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueAcademiqueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAcademique.setId(longCount.incrementAndGet());

        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriqueAcademique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAcademique.setId(longCount.incrementAndGet());

        // Create the HistoriqueAcademique
        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueAcademiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueAcademique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriqueAcademique() throws Exception {
        // Initialize the database
        insertedHistoriqueAcademique = historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiqueAcademique
        restHistoriqueAcademiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueAcademique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiqueAcademiqueRepository.count();
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

    protected HistoriqueAcademique getPersistedHistoriqueAcademique(HistoriqueAcademique historiqueAcademique) {
        return historiqueAcademiqueRepository.findById(historiqueAcademique.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueAcademiqueToMatchAllProperties(HistoriqueAcademique expectedHistoriqueAcademique) {
        assertHistoriqueAcademiqueAllPropertiesEquals(
            expectedHistoriqueAcademique,
            getPersistedHistoriqueAcademique(expectedHistoriqueAcademique)
        );
    }

    protected void assertPersistedHistoriqueAcademiqueToMatchUpdatableProperties(HistoriqueAcademique expectedHistoriqueAcademique) {
        assertHistoriqueAcademiqueAllUpdatablePropertiesEquals(
            expectedHistoriqueAcademique,
            getPersistedHistoriqueAcademique(expectedHistoriqueAcademique)
        );
    }
}
