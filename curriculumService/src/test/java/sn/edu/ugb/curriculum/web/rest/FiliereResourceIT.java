package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.FiliereAsserts.*;
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
import sn.edu.ugb.curriculum.domain.Filiere;
import sn.edu.ugb.curriculum.repository.FiliereRepository;
import sn.edu.ugb.curriculum.service.dto.FiliereDTO;
import sn.edu.ugb.curriculum.service.mapper.FiliereMapper;

/**
 * Integration tests for the {@link FiliereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FiliereResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private FiliereMapper filiereMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiliereMockMvc;

    private Filiere filiere;

    private Filiere insertedFiliere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiere createEntity() {
        return new Filiere().nom(DEFAULT_NOM).code(DEFAULT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiere createUpdatedEntity() {
        return new Filiere().nom(UPDATED_NOM).code(UPDATED_CODE);
    }

    @BeforeEach
    void initTest() {
        filiere = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFiliere != null) {
            filiereRepository.delete(insertedFiliere);
            insertedFiliere = null;
        }
    }

    @Test
    @Transactional
    void createFiliere() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);
        var returnedFiliereDTO = om.readValue(
            restFiliereMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiereDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FiliereDTO.class
        );

        // Validate the Filiere in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFiliere = filiereMapper.toEntity(returnedFiliereDTO);
        assertFiliereUpdatableFieldsEquals(returnedFiliere, getPersistedFiliere(returnedFiliere));

        insertedFiliere = returnedFiliere;
    }

    @Test
    @Transactional
    void createFiliereWithExistingId() throws Exception {
        // Create the Filiere with an existing ID
        filiere.setId(1L);
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiliereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiereDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        filiere.setNom(null);

        // Create the Filiere, which fails.
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        restFiliereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiereDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        filiere.setCode(null);

        // Create the Filiere, which fails.
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        restFiliereMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiereDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFilieres() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList
        restFiliereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getFiliere() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        // Get the filiere
        restFiliereMockMvc
            .perform(get(ENTITY_API_URL_ID, filiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filiere.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingFiliere() throws Exception {
        // Get the filiere
        restFiliereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiliere() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filiere
        Filiere updatedFiliere = filiereRepository.findById(filiere.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiliere are not directly saved in db
        em.detach(updatedFiliere);
        updatedFiliere.nom(UPDATED_NOM).code(UPDATED_CODE);
        FiliereDTO filiereDTO = filiereMapper.toDto(updatedFiliere);

        restFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filiereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(filiereDTO))
            )
            .andExpect(status().isOk());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFiliereToMatchAllProperties(updatedFiliere);
    }

    @Test
    @Transactional
    void putNonExistingFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filiereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(filiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(filiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiereDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFiliereWithPatch() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filiere using partial update
        Filiere partialUpdatedFiliere = new Filiere();
        partialUpdatedFiliere.setId(filiere.getId());

        partialUpdatedFiliere.code(UPDATED_CODE);

        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiliere))
            )
            .andExpect(status().isOk());

        // Validate the Filiere in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiliereUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFiliere, filiere), getPersistedFiliere(filiere));
    }

    @Test
    @Transactional
    void fullUpdateFiliereWithPatch() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filiere using partial update
        Filiere partialUpdatedFiliere = new Filiere();
        partialUpdatedFiliere.setId(filiere.getId());

        partialUpdatedFiliere.nom(UPDATED_NOM).code(UPDATED_CODE);

        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiliere))
            )
            .andExpect(status().isOk());

        // Validate the Filiere in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiliereUpdatableFieldsEquals(partialUpdatedFiliere, getPersistedFiliere(partialUpdatedFiliere));
    }

    @Test
    @Transactional
    void patchNonExistingFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filiereDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(filiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(filiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // Create the Filiere
        FiliereDTO filiereDTO = filiereMapper.toDto(filiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filiereDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiliere() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the filiere
        restFiliereMockMvc
            .perform(delete(ENTITY_API_URL_ID, filiere.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return filiereRepository.count();
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

    protected Filiere getPersistedFiliere(Filiere filiere) {
        return filiereRepository.findById(filiere.getId()).orElseThrow();
    }

    protected void assertPersistedFiliereToMatchAllProperties(Filiere expectedFiliere) {
        assertFiliereAllPropertiesEquals(expectedFiliere, getPersistedFiliere(expectedFiliere));
    }

    protected void assertPersistedFiliereToMatchUpdatableProperties(Filiere expectedFiliere) {
        assertFiliereAllUpdatablePropertiesEquals(expectedFiliere, getPersistedFiliere(expectedFiliere));
    }
}
