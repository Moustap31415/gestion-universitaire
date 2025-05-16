package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.edu.ugb.student.domain.HistoriqueAcademique;
import sn.edu.ugb.student.domain.enumeration.StatutAcademique;
import sn.edu.ugb.student.repository.EtudiantRepository;
import sn.edu.ugb.student.repository.HistoriqueAcademiqueRepository;
import sn.edu.ugb.student.service.dto.HistoriqueAcademiqueDTO;
import sn.edu.ugb.student.service.mapper.HistoriqueAcademiqueMapper;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueAcademiqueResourceIT {

    private static final StatutAcademique DEFAULT_STATUT = StatutAcademique.EN_COURS;
    private static final StatutAcademique UPDATED_STATUT = StatutAcademique.VALIDE;

    private static final Instant DEFAULT_DATE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_SEMESTRE_ID = 1L;
    private static final Long UPDATED_SEMESTRE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/historique-academiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoriqueAcademiqueRepository historiqueAcademiqueRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private HistoriqueAcademiqueMapper historiqueAcademiqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueAcademiqueMockMvc;

    private HistoriqueAcademique historiqueAcademique;
    private Etudiant etudiant;

    public static Etudiant createEtudiantEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .numeroEtudiant("ETD" + longCount.incrementAndGet())
            .utilisateurId(longCount.incrementAndGet());
        em.persist(etudiant);
        em.flush();
        return etudiant;
    }

    public static HistoriqueAcademique createEntity(EntityManager em) {
        Etudiant etudiant = createEtudiantEntity(em);
        return new HistoriqueAcademique()
            .statut(DEFAULT_STATUT)
            .dateInscription(DEFAULT_DATE_INSCRIPTION)
            .etudiant(etudiant)
            .semestreId(DEFAULT_SEMESTRE_ID);
    }

    public static HistoriqueAcademique createUpdatedEntity(EntityManager em) {
        Etudiant etudiant = createEtudiantEntity(em);
        return new HistoriqueAcademique()
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .etudiant(etudiant)
            .semestreId(UPDATED_SEMESTRE_ID);
    }

    @BeforeEach
    public void initTest() {
        etudiant = createEtudiantEntity(em);
        historiqueAcademique = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoriqueAcademique() throws Exception {
        int databaseSizeBeforeCreate = historiqueAcademiqueRepository.findAll().size();

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(historiqueAcademiqueDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateInscription").exists())
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));

        List<HistoriqueAcademique> historiqueList = historiqueAcademiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    void createHistoriqueAcademiqueWithExistingId() throws Exception {
        historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);
        int databaseSizeBeforeCreate = historiqueAcademiqueRepository.findAll().size();
        historiqueAcademique.setId(longCount.incrementAndGet());

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(historiqueAcademiqueDTO)))
            .andExpect(status().isBadRequest());

        assertThat(historiqueAcademiqueRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueAcademiqueRepository.findAll().size();
        historiqueAcademique.setStatut(null);

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(historiqueAcademiqueDTO)))
            .andExpect(status().isBadRequest());

        assertThat(historiqueAcademiqueRepository.findAll()).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateInscriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueAcademiqueRepository.findAll().size();
        historiqueAcademique.setDateInscription(null);

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(historiqueAcademiqueDTO)))
            .andExpect(status().isBadRequest());

        assertThat(historiqueAcademiqueRepository.findAll()).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueAcademiques() throws Exception {
        historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueAcademique.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].semestreId").value(hasItem(DEFAULT_SEMESTRE_ID.intValue())));
    }

    @Test
    @Transactional
    void getHistoriqueAcademique() throws Exception {
        historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueAcademique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueAcademique.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.semestreId").value(DEFAULT_SEMESTRE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueAcademique() throws Exception {
        restHistoriqueAcademiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateHistoriqueAcademique() throws Exception {
        historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);
        int databaseSizeBeforeUpdate = historiqueAcademiqueRepository.findAll().size();

        HistoriqueAcademique updatedHistoriqueAcademique = historiqueAcademiqueRepository.findById(historiqueAcademique.getId()).get();
        em.detach(updatedHistoriqueAcademique);
        updatedHistoriqueAcademique
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .semestreId(UPDATED_SEMESTRE_ID);

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(updatedHistoriqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(put(ENTITY_API_URL_ID, historiqueAcademiqueDTO.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(historiqueAcademiqueDTO)))
            .andExpect(status().isOk());

        List<HistoriqueAcademique> historiqueList = historiqueAcademiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueAcademique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testHistorique.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testHistorique.getSemestreId()).isEqualTo(UPDATED_SEMESTRE_ID);
    }

    @Test
    @Transactional
    void updateNonExistingHistoriqueAcademique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueAcademiqueRepository.findAll().size();
        historiqueAcademique.setId(longCount.incrementAndGet());

        HistoriqueAcademiqueDTO historiqueAcademiqueDTO = historiqueAcademiqueMapper.toDto(historiqueAcademique);

        restHistoriqueAcademiqueMockMvc
            .perform(put(ENTITY_API_URL_ID, historiqueAcademiqueDTO.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(historiqueAcademiqueDTO)))
            .andExpect(status().isBadRequest());

        assertThat(historiqueAcademiqueRepository.findAll()).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueAcademique() throws Exception {
        historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);
        int databaseSizeBeforeUpdate = historiqueAcademiqueRepository.findAll().size();

        HistoriqueAcademique partialUpdatedHistoriqueAcademique = new HistoriqueAcademique();
        partialUpdatedHistoriqueAcademique.setId(historiqueAcademique.getId());

        partialUpdatedHistoriqueAcademique
            .statut(UPDATED_STATUT)
            .dateInscription(UPDATED_DATE_INSCRIPTION);

        restHistoriqueAcademiqueMockMvc
            .perform(patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAcademique.getId())
                .with(csrf())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsBytes(partialUpdatedHistoriqueAcademique)))
            .andExpect(status().isOk());

        List<HistoriqueAcademique> historiqueList = historiqueAcademiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueAcademique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testHistorique.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testHistorique.getSemestreId()).isEqualTo(DEFAULT_SEMESTRE_ID);
    }

    @Test
    @Transactional
    void deleteHistoriqueAcademique() throws Exception {
        historiqueAcademiqueRepository.saveAndFlush(historiqueAcademique);
        int databaseSizeBeforeDelete = historiqueAcademiqueRepository.findAll().size();

        restHistoriqueAcademiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueAcademique.getId())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(historiqueAcademiqueRepository.findAll()).hasSize(databaseSizeBeforeDelete - 1);
    }
}
