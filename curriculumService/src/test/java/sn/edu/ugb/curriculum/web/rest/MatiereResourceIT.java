package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.MatiereAsserts.*;
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
import sn.edu.ugb.curriculum.domain.Matiere;
import sn.edu.ugb.curriculum.domain.UniteEnseignement;
import sn.edu.ugb.curriculum.repository.MatiereRepository;
import sn.edu.ugb.curriculum.repository.UniteEnseignementRepository;
import sn.edu.ugb.curriculum.service.dto.MatiereDTO;
import sn.edu.ugb.curriculum.service.mapper.MatiereMapper;

/**
 * Integration tests for the {@link MatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatiereResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEURES = 1;
    private static final Integer UPDATED_HEURES = 2;

    private static final Integer DEFAULT_CREDITS = 1;
    private static final Integer UPDATED_CREDITS = 2;

    private static final String ENTITY_API_URL = "/api/matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private UniteEnseignementRepository uniteEnseignementRepository;

    @Autowired
    private MatiereMapper matiereMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatiereMockMvc;

    private Matiere matiere;
    private UniteEnseignement uniteEnseignement;
    private Matiere insertedMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createEntity(EntityManager em) {
        UniteEnseignement uniteEnseignement = new UniteEnseignement()
            .nom("UE Test")
            .code("UE001");
        em.persist(uniteEnseignement);
        em.flush();

        return new Matiere()
            .nom(DEFAULT_NOM)
            .heures(DEFAULT_HEURES)
            .credits(DEFAULT_CREDITS)
            .uniteEnseignement(uniteEnseignement);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createUpdatedEntity(EntityManager em) {
        UniteEnseignement uniteEnseignement = new UniteEnseignement()
            .nom("UE Test Updated")
            .code("UE002");
        em.persist(uniteEnseignement);
        em.flush();

        return new Matiere()
            .nom(UPDATED_NOM)
            .heures(UPDATED_HEURES)
            .credits(UPDATED_CREDITS)
            .uniteEnseignement(uniteEnseignement);
    }

    @BeforeEach
    void initTest() {
        uniteEnseignement = new UniteEnseignement()
            .nom("UE Test")
            .code("UE001");
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        matiere = new Matiere()
            .nom(DEFAULT_NOM)
            .heures(DEFAULT_HEURES)
            .credits(DEFAULT_CREDITS)
            .uniteEnseignement(uniteEnseignement);
    }

    @AfterEach
    void cleanup() {
        if (insertedMatiere != null) {
            matiereRepository.delete(insertedMatiere);
            insertedMatiere = null;
        }
        if (uniteEnseignement != null) {
            uniteEnseignementRepository.delete(uniteEnseignement);
            uniteEnseignement = null;
        }
    }

    @Test
    @Transactional
    void createMatiere() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);
        var returnedMatiereDTO = om.readValue(
            restMatiereMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MatiereDTO.class
        );

        // Validate the Matiere in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMatiere = matiereMapper.toEntity(returnedMatiereDTO);
        assertMatiereUpdatableFieldsEquals(returnedMatiere, getPersistedMatiere(returnedMatiere));
        assertThat(returnedMatiere.getUniteEnseignement().getId()).isEqualTo(uniteEnseignement.getId());

        insertedMatiere = returnedMatiere;
    }

    @Test
    @Transactional
    void createMatiereWithExistingId() throws Exception {
        // Create the Matiere with an existing ID
        matiere.setId(1L);
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matiere.setNom(null);

        // Create the Matiere, which fails.
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeuresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matiere.setHeures(null);

        // Create the Matiere, which fails.
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matiere.setCredits(null);

        // Create the Matiere, which fails.
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUniteEnseignementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matiere.setUniteEnseignement(null);

        // Create the Matiere, which fails.
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatieres() throws Exception {
        // Initialize the database
        insertedMatiere = matiereRepository.saveAndFlush(matiere);

        // Get all the matiereList
        restMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].heures").value(hasItem(DEFAULT_HEURES)))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)))
            .andExpect(jsonPath("$.[*].uniteEnseignement.id").value(hasItem(uniteEnseignement.getId().intValue())));
    }

    @Test
    @Transactional
    void getMatiere() throws Exception {
        // Initialize the database
        insertedMatiere = matiereRepository.saveAndFlush(matiere);

        // Get the matiere
        restMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, matiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matiere.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.heures").value(DEFAULT_HEURES))
            .andExpect(jsonPath("$.credits").value(DEFAULT_CREDITS))
            .andExpect(jsonPath("$.uniteEnseignement.id").value(uniteEnseignement.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMatiere() throws Exception {
        // Get the matiere
        restMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatiere() throws Exception {
        // Initialize the database
        insertedMatiere = matiereRepository.saveAndFlush(matiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matiere
        Matiere updatedMatiere = matiereRepository.findById(matiere.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMatiere are not directly saved in db
        em.detach(updatedMatiere);

        UniteEnseignement updatedUniteEnseignement = new UniteEnseignement()
            .nom("UE Updated")
            .code("UE002");
        uniteEnseignementRepository.saveAndFlush(updatedUniteEnseignement);

        updatedMatiere
            .nom(UPDATED_NOM)
            .heures(UPDATED_HEURES)
            .credits(UPDATED_CREDITS)
            .uniteEnseignement(updatedUniteEnseignement);

        MatiereDTO matiereDTO = matiereMapper.toDto(updatedMatiere);

        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matiereDTO))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        Matiere persistedMatiere = matiereRepository.findById(updatedMatiere.getId()).get();
        assertThat(persistedMatiere.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(persistedMatiere.getHeures()).isEqualTo(UPDATED_HEURES);
        assertThat(persistedMatiere.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(persistedMatiere.getUniteEnseignement().getId()).isEqualTo(updatedUniteEnseignement.getId());
    }

    @Test
    @Transactional
    void putNonExistingMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiere.setId(longCount.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiere.setId(longCount.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiere.setId(longCount.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatiereWithPatch() throws Exception {
        // Initialize the database
        insertedMatiere = matiereRepository.saveAndFlush(matiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matiere using partial update
        Matiere partialUpdatedMatiere = new Matiere();
        partialUpdatedMatiere.setId(matiere.getId());

        partialUpdatedMatiere.heures(UPDATED_HEURES).credits(UPDATED_CREDITS);

        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        Matiere persistedMatiere = matiereRepository.findById(matiere.getId()).get();
        assertThat(persistedMatiere.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(persistedMatiere.getHeures()).isEqualTo(UPDATED_HEURES);
        assertThat(persistedMatiere.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(persistedMatiere.getUniteEnseignement().getId()).isEqualTo(uniteEnseignement.getId());
    }

    @Test
    @Transactional
    void fullUpdateMatiereWithPatch() throws Exception {
        // Initialize the database
        insertedMatiere = matiereRepository.saveAndFlush(matiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matiere using partial update
        Matiere partialUpdatedMatiere = new Matiere();
        partialUpdatedMatiere.setId(matiere.getId());

        UniteEnseignement newUniteEnseignement = new UniteEnseignement()
            .nom("UE New")
            .code("UE003");
        uniteEnseignementRepository.saveAndFlush(newUniteEnseignement);

        partialUpdatedMatiere
            .nom(UPDATED_NOM)
            .heures(UPDATED_HEURES)
            .credits(UPDATED_CREDITS)
            .uniteEnseignement(newUniteEnseignement);

        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        Matiere persistedMatiere = matiereRepository.findById(matiere.getId()).get();
        assertThat(persistedMatiere.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(persistedMatiere.getHeures()).isEqualTo(UPDATED_HEURES);
        assertThat(persistedMatiere.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(persistedMatiere.getUniteEnseignement().getId()).isEqualTo(newUniteEnseignement.getId());
    }

    @Test
    @Transactional
    void patchNonExistingMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiere.setId(longCount.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matiereDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiere.setId(longCount.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiere.setId(longCount.incrementAndGet());

        // Create the Matiere
        MatiereDTO matiereDTO = matiereMapper.toDto(matiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(matiereDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatiere() throws Exception {
        // Initialize the database
        insertedMatiere = matiereRepository.saveAndFlush(matiere);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the matiere
        restMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, matiere.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return matiereRepository.count();
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

    protected Matiere getPersistedMatiere(Matiere matiere) {
        return matiereRepository.findById(matiere.getId()).orElseThrow();
    }

    protected void assertPersistedMatiereToMatchAllProperties(Matiere expectedMatiere) {
        assertMatiereAllPropertiesEquals(expectedMatiere, getPersistedMatiere(expectedMatiere));
    }

    protected void assertPersistedMatiereToMatchUpdatableProperties(Matiere expectedMatiere) {
        assertMatiereAllUpdatablePropertiesEquals(expectedMatiere, getPersistedMatiere(expectedMatiere));
    }
}
