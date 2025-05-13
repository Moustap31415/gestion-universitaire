package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.UniteEnseignementAsserts.*;
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
import sn.edu.ugb.curriculum.domain.UniteEnseignement;
import sn.edu.ugb.curriculum.repository.UniteEnseignementRepository;
import sn.edu.ugb.curriculum.service.dto.UniteEnseignementDTO;
import sn.edu.ugb.curriculum.service.mapper.UniteEnseignementMapper;

/**
 * Integration tests for the {@link UniteEnseignementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UniteEnseignementResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_FILIERE_ID = 1L;
    private static final Long UPDATED_FILIERE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/unite-enseignements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UniteEnseignementRepository uniteEnseignementRepository;

    @Autowired
    private UniteEnseignementMapper uniteEnseignementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniteEnseignementMockMvc;

    private UniteEnseignement uniteEnseignement;

    private UniteEnseignement insertedUniteEnseignement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniteEnseignement createEntity() {
        return new UniteEnseignement().nom(DEFAULT_NOM).code(DEFAULT_CODE).filiereId(DEFAULT_FILIERE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniteEnseignement createUpdatedEntity() {
        return new UniteEnseignement().nom(UPDATED_NOM).code(UPDATED_CODE).filiereId(UPDATED_FILIERE_ID);
    }

    @BeforeEach
    void initTest() {
        uniteEnseignement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUniteEnseignement != null) {
            uniteEnseignementRepository.delete(insertedUniteEnseignement);
            insertedUniteEnseignement = null;
        }
    }

    @Test
    @Transactional
    void createUniteEnseignement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);
        var returnedUniteEnseignementDTO = om.readValue(
            restUniteEnseignementMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(uniteEnseignementDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UniteEnseignementDTO.class
        );

        // Validate the UniteEnseignement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUniteEnseignement = uniteEnseignementMapper.toEntity(returnedUniteEnseignementDTO);
        assertUniteEnseignementUpdatableFieldsEquals(returnedUniteEnseignement, getPersistedUniteEnseignement(returnedUniteEnseignement));

        insertedUniteEnseignement = returnedUniteEnseignement;
    }

    @Test
    @Transactional
    void createUniteEnseignementWithExistingId() throws Exception {
        // Create the UniteEnseignement with an existing ID
        uniteEnseignement.setId(1L);
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniteEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uniteEnseignement.setNom(null);

        // Create the UniteEnseignement, which fails.
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uniteEnseignement.setCode(null);

        // Create the UniteEnseignement, which fails.
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiliereIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uniteEnseignement.setFiliereId(null);

        // Create the UniteEnseignement, which fails.
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUniteEnseignements() throws Exception {
        // Initialize the database
        insertedUniteEnseignement = uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        // Get all the uniteEnseignementList
        restUniteEnseignementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uniteEnseignement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].filiereId").value(hasItem(DEFAULT_FILIERE_ID.intValue())));
    }

    @Test
    @Transactional
    void getUniteEnseignement() throws Exception {
        // Initialize the database
        insertedUniteEnseignement = uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        // Get the uniteEnseignement
        restUniteEnseignementMockMvc
            .perform(get(ENTITY_API_URL_ID, uniteEnseignement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uniteEnseignement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.filiereId").value(DEFAULT_FILIERE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUniteEnseignement() throws Exception {
        // Get the uniteEnseignement
        restUniteEnseignementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUniteEnseignement() throws Exception {
        // Initialize the database
        insertedUniteEnseignement = uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uniteEnseignement
        UniteEnseignement updatedUniteEnseignement = uniteEnseignementRepository.findById(uniteEnseignement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUniteEnseignement are not directly saved in db
        em.detach(updatedUniteEnseignement);
        updatedUniteEnseignement.nom(UPDATED_NOM).code(UPDATED_CODE).filiereId(UPDATED_FILIERE_ID);
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(updatedUniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uniteEnseignementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isOk());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUniteEnseignementToMatchAllProperties(updatedUniteEnseignement);
    }

    @Test
    @Transactional
    void putNonExistingUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteEnseignement.setId(longCount.incrementAndGet());

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniteEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uniteEnseignementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteEnseignement.setId(longCount.incrementAndGet());

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteEnseignement.setId(longCount.incrementAndGet());

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUniteEnseignementWithPatch() throws Exception {
        // Initialize the database
        insertedUniteEnseignement = uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uniteEnseignement using partial update
        UniteEnseignement partialUpdatedUniteEnseignement = new UniteEnseignement();
        partialUpdatedUniteEnseignement.setId(uniteEnseignement.getId());

        partialUpdatedUniteEnseignement.nom(UPDATED_NOM);

        restUniteEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniteEnseignement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUniteEnseignement))
            )
            .andExpect(status().isOk());

        // Validate the UniteEnseignement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUniteEnseignementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUniteEnseignement, uniteEnseignement),
            getPersistedUniteEnseignement(uniteEnseignement)
        );
    }

    @Test
    @Transactional
    void fullUpdateUniteEnseignementWithPatch() throws Exception {
        // Initialize the database
        insertedUniteEnseignement = uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uniteEnseignement using partial update
        UniteEnseignement partialUpdatedUniteEnseignement = new UniteEnseignement();
        partialUpdatedUniteEnseignement.setId(uniteEnseignement.getId());

        partialUpdatedUniteEnseignement.nom(UPDATED_NOM).code(UPDATED_CODE).filiereId(UPDATED_FILIERE_ID);

        restUniteEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniteEnseignement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUniteEnseignement))
            )
            .andExpect(status().isOk());

        // Validate the UniteEnseignement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUniteEnseignementUpdatableFieldsEquals(
            partialUpdatedUniteEnseignement,
            getPersistedUniteEnseignement(partialUpdatedUniteEnseignement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteEnseignement.setId(longCount.incrementAndGet());

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniteEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uniteEnseignementDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteEnseignement.setId(longCount.incrementAndGet());

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uniteEnseignement.setId(longCount.incrementAndGet());

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uniteEnseignementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniteEnseignement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUniteEnseignement() throws Exception {
        // Initialize the database
        insertedUniteEnseignement = uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uniteEnseignement
        restUniteEnseignementMockMvc
            .perform(delete(ENTITY_API_URL_ID, uniteEnseignement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uniteEnseignementRepository.count();
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

    protected UniteEnseignement getPersistedUniteEnseignement(UniteEnseignement uniteEnseignement) {
        return uniteEnseignementRepository.findById(uniteEnseignement.getId()).orElseThrow();
    }

    protected void assertPersistedUniteEnseignementToMatchAllProperties(UniteEnseignement expectedUniteEnseignement) {
        assertUniteEnseignementAllPropertiesEquals(expectedUniteEnseignement, getPersistedUniteEnseignement(expectedUniteEnseignement));
    }

    protected void assertPersistedUniteEnseignementToMatchUpdatableProperties(UniteEnseignement expectedUniteEnseignement) {
        assertUniteEnseignementAllUpdatablePropertiesEquals(
            expectedUniteEnseignement,
            getPersistedUniteEnseignement(expectedUniteEnseignement)
        );
    }
}
