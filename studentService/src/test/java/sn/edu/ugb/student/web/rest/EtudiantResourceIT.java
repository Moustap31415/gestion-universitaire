package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.student.domain.EtudiantAsserts.*;
import static sn.edu.ugb.student.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.student.IntegrationTest;
import sn.edu.ugb.student.domain.Etudiant;
import sn.edu.ugb.student.repository.EtudiantRepository;
import sn.edu.ugb.student.service.dto.EtudiantDTO;
import sn.edu.ugb.student.service.mapper.EtudiantMapper;

/**
 * Integration tests for the {@link EtudiantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtudiantResourceIT {

    private static final String DEFAULT_NUMERO_ETUDIANT = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ETUDIANT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_FORMATION_ACTUELLE = "AAAAAAAAAA";
    private static final String UPDATED_FORMATION_ACTUELLE = "BBBBBBBBBB";

    private static final Long DEFAULT_UTILISATEUR_ID = 1L;
    private static final Long UPDATED_UTILISATEUR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EtudiantMapper etudiantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    private Etudiant insertedEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity() {
        return new Etudiant()
            .numeroEtudiant(DEFAULT_NUMERO_ETUDIANT)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .adresse(DEFAULT_ADRESSE)
            .formationActuelle(DEFAULT_FORMATION_ACTUELLE)
            .utilisateurId(DEFAULT_UTILISATEUR_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createUpdatedEntity() {
        return new Etudiant()
            .numeroEtudiant(UPDATED_NUMERO_ETUDIANT)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresse(UPDATED_ADRESSE)
            .formationActuelle(UPDATED_FORMATION_ACTUELLE)
            .utilisateurId(UPDATED_UTILISATEUR_ID);
    }

    @BeforeEach
    void initTest() {
        etudiant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEtudiant != null) {
            etudiantRepository.delete(insertedEtudiant);
            insertedEtudiant = null;
        }
    }

    @Test
    @Transactional
    void createEtudiant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);
        var returnedEtudiantDTO = om.readValue(
            restEtudiantMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EtudiantDTO.class
        );

        // Validate the Etudiant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEtudiant = etudiantMapper.toEntity(returnedEtudiantDTO);
        assertEtudiantUpdatableFieldsEquals(returnedEtudiant, getPersistedEtudiant(returnedEtudiant));

        insertedEtudiant = returnedEtudiant;
    }

    @Test
    @Transactional
    void createEtudiantWithExistingId() throws Exception {
        // Create the Etudiant with an existing ID
        etudiant.setId(1L);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroEtudiantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setNumeroEtudiant(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUtilisateurIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setUtilisateurId(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtudiants() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEtudiant").value(hasItem(DEFAULT_NUMERO_ETUDIANT)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].formationActuelle").value(hasItem(DEFAULT_FORMATION_ACTUELLE)))
            .andExpect(jsonPath("$.[*].utilisateurId").value(hasItem(DEFAULT_UTILISATEUR_ID.intValue())));
    }

    @Test
    @Transactional
    void getEtudiant() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.numeroEtudiant").value(DEFAULT_NUMERO_ETUDIANT))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.formationActuelle").value(DEFAULT_FORMATION_ACTUELLE))
            .andExpect(jsonPath("$.utilisateurId").value(DEFAULT_UTILISATEUR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtudiant() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtudiant are not directly saved in db
        em.detach(updatedEtudiant);
        updatedEtudiant
            .numeroEtudiant(UPDATED_NUMERO_ETUDIANT)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresse(UPDATED_ADRESSE)
            .formationActuelle(UPDATED_FORMATION_ACTUELLE)
            .utilisateurId(UPDATED_UTILISATEUR_ID);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(updatedEtudiant);

        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEtudiantToMatchAllProperties(updatedEtudiant);
    }

    @Test
    @Transactional
    void putNonExistingEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .numeroEtudiant(UPDATED_NUMERO_ETUDIANT)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .formationActuelle(UPDATED_FORMATION_ACTUELLE);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtudiantUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEtudiant, etudiant), getPersistedEtudiant(etudiant));
    }

    @Test
    @Transactional
    void fullUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .numeroEtudiant(UPDATED_NUMERO_ETUDIANT)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .adresse(UPDATED_ADRESSE)
            .formationActuelle(UPDATED_FORMATION_ACTUELLE)
            .utilisateurId(UPDATED_UTILISATEUR_ID);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtudiantUpdatableFieldsEquals(partialUpdatedEtudiant, getPersistedEtudiant(partialUpdatedEtudiant));
    }

    @Test
    @Transactional
    void patchNonExistingEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtudiant() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the etudiant
        restEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, etudiant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return etudiantRepository.count();
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

    protected Etudiant getPersistedEtudiant(Etudiant etudiant) {
        return etudiantRepository.findById(etudiant.getId()).orElseThrow();
    }

    protected void assertPersistedEtudiantToMatchAllProperties(Etudiant expectedEtudiant) {
        assertEtudiantAllPropertiesEquals(expectedEtudiant, getPersistedEtudiant(expectedEtudiant));
    }

    protected void assertPersistedEtudiantToMatchUpdatableProperties(Etudiant expectedEtudiant) {
        assertEtudiantAllUpdatablePropertiesEquals(expectedEtudiant, getPersistedEtudiant(expectedEtudiant));
    }
}
