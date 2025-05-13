package sn.edu.ugb.teacher.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.teacher.domain.EnseignantAsserts.*;
import static sn.edu.ugb.teacher.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.teacher.IntegrationTest;
import sn.edu.ugb.teacher.domain.Enseignant;
import sn.edu.ugb.teacher.repository.EnseignantRepository;
import sn.edu.ugb.teacher.service.dto.EnseignantDTO;
import sn.edu.ugb.teacher.service.mapper.EnseignantMapper;

/**
 * Integration tests for the {@link EnseignantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnseignantResourceIT {

    private static final String DEFAULT_NUMERO_ENSEIGNANT = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ENSEIGNANT = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALISATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALISATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EMBAUCHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMBAUCHE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_UTILISATEUR_ID = 1L;
    private static final Long UPDATED_UTILISATEUR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/enseignants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignantMapper enseignantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    private Enseignant insertedEnseignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity() {
        return new Enseignant()
            .numeroEnseignant(DEFAULT_NUMERO_ENSEIGNANT)
            .specialisation(DEFAULT_SPECIALISATION)
            .dateEmbauche(DEFAULT_DATE_EMBAUCHE)
            .utilisateurId(DEFAULT_UTILISATEUR_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createUpdatedEntity() {
        return new Enseignant()
            .numeroEnseignant(UPDATED_NUMERO_ENSEIGNANT)
            .specialisation(UPDATED_SPECIALISATION)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .utilisateurId(UPDATED_UTILISATEUR_ID);
    }

    @BeforeEach
    void initTest() {
        enseignant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEnseignant != null) {
            enseignantRepository.delete(insertedEnseignant);
            insertedEnseignant = null;
        }
    }

    @Test
    @Transactional
    void createEnseignant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);
        var returnedEnseignantDTO = om.readValue(
            restEnseignantMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnseignantDTO.class
        );

        // Validate the Enseignant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnseignant = enseignantMapper.toEntity(returnedEnseignantDTO);
        assertEnseignantUpdatableFieldsEquals(returnedEnseignant, getPersistedEnseignant(returnedEnseignant));

        insertedEnseignant = returnedEnseignant;
    }

    @Test
    @Transactional
    void createEnseignantWithExistingId() throws Exception {
        // Create the Enseignant with an existing ID
        enseignant.setId(1L);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroEnseignantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setNumeroEnseignant(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUtilisateurIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setUtilisateurId(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnseignants() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEnseignant").value(hasItem(DEFAULT_NUMERO_ENSEIGNANT)))
            .andExpect(jsonPath("$.[*].specialisation").value(hasItem(DEFAULT_SPECIALISATION)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].utilisateurId").value(hasItem(DEFAULT_UTILISATEUR_ID.intValue())));
    }

    @Test
    @Transactional
    void getEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL_ID, enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.numeroEnseignant").value(DEFAULT_NUMERO_ENSEIGNANT))
            .andExpect(jsonPath("$.specialisation").value(DEFAULT_SPECIALISATION))
            .andExpect(jsonPath("$.dateEmbauche").value(DEFAULT_DATE_EMBAUCHE.toString()))
            .andExpect(jsonPath("$.utilisateurId").value(DEFAULT_UTILISATEUR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .numeroEnseignant(UPDATED_NUMERO_ENSEIGNANT)
            .specialisation(UPDATED_SPECIALISATION)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .utilisateurId(UPDATED_UTILISATEUR_ID);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(updatedEnseignant);

        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnseignantToMatchAllProperties(updatedEnseignant);
    }

    @Test
    @Transactional
    void putNonExistingEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant.specialisation(UPDATED_SPECIALISATION).dateEmbauche(UPDATED_DATE_EMBAUCHE);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnseignantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnseignant, enseignant),
            getPersistedEnseignant(enseignant)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .numeroEnseignant(UPDATED_NUMERO_ENSEIGNANT)
            .specialisation(UPDATED_SPECIALISATION)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .utilisateurId(UPDATED_UTILISATEUR_ID);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnseignantUpdatableFieldsEquals(partialUpdatedEnseignant, getPersistedEnseignant(partialUpdatedEnseignant));
    }

    @Test
    @Transactional
    void patchNonExistingEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enseignant
        restEnseignantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enseignantRepository.count();
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

    protected Enseignant getPersistedEnseignant(Enseignant enseignant) {
        return enseignantRepository.findById(enseignant.getId()).orElseThrow();
    }

    protected void assertPersistedEnseignantToMatchAllProperties(Enseignant expectedEnseignant) {
        assertEnseignantAllPropertiesEquals(expectedEnseignant, getPersistedEnseignant(expectedEnseignant));
    }

    protected void assertPersistedEnseignantToMatchUpdatableProperties(Enseignant expectedEnseignant) {
        assertEnseignantAllUpdatablePropertiesEquals(expectedEnseignant, getPersistedEnseignant(expectedEnseignant));
    }
}
