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
import sn.edu.ugb.curriculum.domain.UniteEnseignement;
import sn.edu.ugb.curriculum.repository.FiliereRepository;
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

    private static final String ENTITY_API_URL = "/api/unite-enseignements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UniteEnseignementRepository uniteEnseignementRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private UniteEnseignementMapper uniteEnseignementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniteEnseignementMockMvc;

    private UniteEnseignement uniteEnseignement;
    private Filiere filiere;

    /**
     * Create an entity for this test.
     */
    public static UniteEnseignement createEntity(EntityManager em) {
        Filiere filiere = new Filiere()
            .nom("Filiere Test")
            .code("FIL123");
        em.persist(filiere);
        em.flush();

        return new UniteEnseignement()
            .nom(DEFAULT_NOM)
            .code(DEFAULT_CODE)
            .filiere(filiere);
    }

    /**
     * Create an updated entity for this test.
     */
    public static UniteEnseignement createUpdatedEntity(EntityManager em) {
        Filiere filiere = new Filiere()
            .nom("Filiere Test Updated")
            .code("FIL456");
        em.persist(filiere);
        em.flush();

        return new UniteEnseignement()
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .filiere(filiere);
    }

    @BeforeEach
    public void initTest() {
        filiere = new Filiere()
            .nom("Filiere Test")
            .code("FIL123");
        filiereRepository.saveAndFlush(filiere);

        uniteEnseignement = new UniteEnseignement()
            .nom(DEFAULT_NOM)
            .code(DEFAULT_CODE)
            .filiere(filiere);
    }

    @Test
    @Transactional
    void createUniteEnseignement() throws Exception {
        long databaseSizeBeforeCreate = uniteEnseignementRepository.count();

        // Create the UniteEnseignement
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.filiere.id").value(filiere.getId().intValue()));

        // Validate the database
        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    void createUniteEnseignementWithExistingId() throws Exception {
        uniteEnseignement.setId(1L);
        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        long databaseSizeBeforeCreate = uniteEnseignementRepository.count();

        restUniteEnseignementMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = uniteEnseignementRepository.count();
        uniteEnseignement.setNom(null);

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = uniteEnseignementRepository.count();
        uniteEnseignement.setCode(null);

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiliereIsRequired() throws Exception {
        long databaseSizeBeforeTest = uniteEnseignementRepository.count();
        uniteEnseignement.setFiliere(null);

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUniteEnseignements() throws Exception {
        // Initialize the database
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        // Get all the uniteEnseignementList
        restUniteEnseignementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uniteEnseignement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].filiere.id").value(hasItem(filiere.getId().intValue())));
    }

    @Test
    @Transactional
    void getUniteEnseignement() throws Exception {
        // Initialize the database
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        // Get the uniteEnseignement
        restUniteEnseignementMockMvc
            .perform(get(ENTITY_API_URL_ID, uniteEnseignement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uniteEnseignement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.filiere.id").value(filiere.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUniteEnseignement() throws Exception {
        restUniteEnseignementMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUniteEnseignement() throws Exception {
        // Initialize the database
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();

        // Update the uniteEnseignement
        UniteEnseignement updatedUniteEnseignement = uniteEnseignementRepository.findById(uniteEnseignement.getId()).get();
        em.detach(updatedUniteEnseignement);

        Filiere updatedFiliere = new Filiere()
            .nom("Updated Filiere")
            .code("FIL456");
        filiereRepository.saveAndFlush(updatedFiliere);

        updatedUniteEnseignement
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .filiere(updatedFiliere);

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(updatedUniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(put(ENTITY_API_URL_ID, uniteEnseignementDTO.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isOk());

        // Validate the UniteEnseignement in the database
        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
        UniteEnseignement persistedUniteEnseignement = uniteEnseignementRepository.findById(uniteEnseignement.getId()).get();
        assertThat(persistedUniteEnseignement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(persistedUniteEnseignement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(persistedUniteEnseignement.getFiliere().getId()).isEqualTo(updatedFiliere.getId());
    }

    @Test
    @Transactional
    void putNonExistingUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();
        uniteEnseignement.setId(longCount.incrementAndGet());

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(put(ENTITY_API_URL_ID, uniteEnseignementDTO.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();
        uniteEnseignement.setId(longCount.incrementAndGet());

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();
        uniteEnseignement.setId(longCount.incrementAndGet());

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(put(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUniteEnseignementWithPatch() throws Exception {
        // Initialize the database
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();

        // Update the uniteEnseignement using partial update
        UniteEnseignement partialUpdatedUniteEnseignement = new UniteEnseignement();
        partialUpdatedUniteEnseignement.setId(uniteEnseignement.getId());

        partialUpdatedUniteEnseignement.nom(UPDATED_NOM);

        restUniteEnseignementMockMvc
            .perform(patch(ENTITY_API_URL_ID, partialUpdatedUniteEnseignement.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(om.writeValueAsBytes(partialUpdatedUniteEnseignement)))
            .andExpect(status().isOk());

        // Validate the database
        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
        UniteEnseignement updatedUniteEnseignement = uniteEnseignementRepository.findById(uniteEnseignement.getId()).get();
        assertThat(updatedUniteEnseignement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(updatedUniteEnseignement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(updatedUniteEnseignement.getFiliere().getId()).isEqualTo(filiere.getId());
    }

    @Test
    @Transactional
    void fullUpdateUniteEnseignementWithPatch() throws Exception {
        // Initialize the database
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();

        // Update the uniteEnseignement using partial update
        UniteEnseignement partialUpdatedUniteEnseignement = new UniteEnseignement();
        partialUpdatedUniteEnseignement.setId(uniteEnseignement.getId());

        Filiere updatedFiliere = new Filiere()
            .nom("Updated Filiere")
            .code("FIL456");
        filiereRepository.saveAndFlush(updatedFiliere);

        partialUpdatedUniteEnseignement
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .filiere(updatedFiliere);

        restUniteEnseignementMockMvc
            .perform(patch(ENTITY_API_URL_ID, partialUpdatedUniteEnseignement.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(om.writeValueAsBytes(partialUpdatedUniteEnseignement)))
            .andExpect(status().isOk());

        // Validate the database
        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
        UniteEnseignement updatedUniteEnseignement = uniteEnseignementRepository.findById(uniteEnseignement.getId()).get();
        assertThat(updatedUniteEnseignement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(updatedUniteEnseignement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(updatedUniteEnseignement.getFiliere().getId()).isEqualTo(updatedFiliere.getId());
    }

    @Test
    @Transactional
    void patchNonExistingUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();
        uniteEnseignement.setId(longCount.incrementAndGet());

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(patch(ENTITY_API_URL_ID, uniteEnseignementDTO.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();
        uniteEnseignement.setId(longCount.incrementAndGet());

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isBadRequest());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniteEnseignement() throws Exception {
        long databaseSizeBeforeUpdate = uniteEnseignementRepository.count();
        uniteEnseignement.setId(longCount.incrementAndGet());

        UniteEnseignementDTO uniteEnseignementDTO = uniteEnseignementMapper.toDto(uniteEnseignement);

        restUniteEnseignementMockMvc
            .perform(patch(ENTITY_API_URL)
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(om.writeValueAsBytes(uniteEnseignementDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUniteEnseignement() throws Exception {
        // Initialize the database
        uniteEnseignementRepository.saveAndFlush(uniteEnseignement);

        long databaseSizeBeforeDelete = uniteEnseignementRepository.count();

        // Delete the uniteEnseignement
        restUniteEnseignementMockMvc
            .perform(delete(ENTITY_API_URL_ID, uniteEnseignement.getId())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertThat(uniteEnseignementRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
    }
}
