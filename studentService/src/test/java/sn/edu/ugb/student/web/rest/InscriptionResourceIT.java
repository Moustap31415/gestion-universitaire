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
import java.util.List;
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
import sn.edu.ugb.student.IntegrationTest;
import sn.edu.ugb.student.domain.Etudiant;
import sn.edu.ugb.student.domain.Inscription;
import sn.edu.ugb.student.repository.EtudiantRepository;
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

    private static final Long DEFAULT_FILIERE_ID = 1L;
    private static final Long UPDATED_FILIERE_ID = 2L;

    private static final Long DEFAULT_SEMESTRE_ID = 1L;
    private static final Long UPDATED_SEMESTRE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     */
    public static Inscription createEntity(EntityManager em) {
        Etudiant etudiant = EtudiantResourceIT.createEntity();
        em.persist(etudiant);
        em.flush();

        return new Inscription()
            .enCours(DEFAULT_EN_COURS)
            .etudiant(etudiant)
            .filiereId(DEFAULT_FILIERE_ID)
            .semestreId(DEFAULT_SEMESTRE_ID);
    }

    /**
     * Create an updated entity for this test.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Etudiant etudiant = EtudiantResourceIT.createEntity();
        em.persist(etudiant);
        em.flush();

        return new Inscription()
            .enCours(UPDATED_EN_COURS)
            .etudiant(etudiant)
            .filiereId(UPDATED_FILIERE_ID)
            .semestreId(UPDATED_SEMESTRE_ID);
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.enCours").value(DEFAULT_EN_COURS))
            .andExpect(jsonPath("$.filiereId").value(DEFAULT_FILIERE_ID.intValue()))
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    void createInscriptionWithExistingId() throws Exception {
        inscription.setId(1L);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnCoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        inscription.setEnCours(null);

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiliereIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        inscription.setFiliereId(null);

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSemestreIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        inscription.setSemestreId(null);

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInscriptions() throws Exception {
        inscriptionRepository.saveAndFlush(inscription);

        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].enCours").value(hasItem(DEFAULT_EN_COURS.booleanValue())))
            .andExpect(jsonPath("$.[*].filiereId").value(hasItem(DEFAULT_FILIERE_ID.intValue())))
            .andExpect(jsonPath("$.[*].semestreId").value(hasItem(DEFAULT_SEMESTRE_ID.intValue())));
    }

    @Test
    @Transactional
    void getInscription() throws Exception {
        inscriptionRepository.saveAndFlush(inscription);

        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.enCours").value(DEFAULT_EN_COURS.booleanValue()))
            .andExpect(jsonPath("$.filiereId").value(DEFAULT_FILIERE_ID.intValue()))
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingInscription() throws Exception {
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscription() throws Exception {
        inscriptionRepository.saveAndFlush(inscription);
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        em.detach(updatedInscription);
        updatedInscription
            .enCours(UPDATED_EN_COURS)
            .filiereId(UPDATED_FILIERE_ID)
            .semestreId(UPDATED_SEMESTRE_ID);

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(updatedInscription);

        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isOk());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getEnCours()).isEqualTo(UPDATED_EN_COURS);
        assertThat(testInscription.getFiliereId()).isEqualTo(UPDATED_FILIERE_ID);
        assertThat(testInscription.getSemestreId()).isEqualTo(UPDATED_SEMESTRE_ID);
    }

    @Test
    @Transactional
    void putNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(longCount.incrementAndGet());

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(longCount.incrementAndGet());

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(longCount.incrementAndGet());

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionWithPatch() throws Exception {
        inscriptionRepository.saveAndFlush(inscription);
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .enCours(UPDATED_EN_COURS)
            .semestreId(UPDATED_SEMESTRE_ID);

        restInscriptionMockMvc
            .perform(patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsBytes(partialUpdatedInscription)))
            .andExpect(status().isOk());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getEnCours()).isEqualTo(UPDATED_EN_COURS);
        assertThat(testInscription.getFiliereId()).isEqualTo(DEFAULT_FILIERE_ID);
        assertThat(testInscription.getSemestreId()).isEqualTo(UPDATED_SEMESTRE_ID);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionWithPatch() throws Exception {
        inscriptionRepository.saveAndFlush(inscription);
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .enCours(UPDATED_EN_COURS)
            .filiereId(UPDATED_FILIERE_ID)
            .semestreId(UPDATED_SEMESTRE_ID);

        restInscriptionMockMvc
            .perform(patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsBytes(partialUpdatedInscription)))
            .andExpect(status().isOk());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getEnCours()).isEqualTo(UPDATED_EN_COURS);
        assertThat(testInscription.getFiliereId()).isEqualTo(UPDATED_FILIERE_ID);
        assertThat(testInscription.getSemestreId()).isEqualTo(UPDATED_SEMESTRE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(longCount.incrementAndGet());

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(patch(ENTITY_API_URL_ID, inscriptionDTO.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(longCount.incrementAndGet());

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(longCount.incrementAndGet());

        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(patch(ENTITY_API_URL)
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsBytes(inscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscription() throws Exception {
        inscriptionRepository.saveAndFlush(inscription);
        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        restInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscription.getId())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
