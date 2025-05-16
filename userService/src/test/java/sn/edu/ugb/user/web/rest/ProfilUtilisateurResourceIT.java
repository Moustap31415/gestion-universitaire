package sn.edu.ugb.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.user.domain.ProfilUtilisateurAsserts.*;
import static sn.edu.ugb.user.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.user.IntegrationTest;
import sn.edu.ugb.user.domain.ProfilUtilisateur;
import sn.edu.ugb.user.domain.Role;
import sn.edu.ugb.user.repository.ProfilUtilisateurRepository;
import sn.edu.ugb.user.repository.RoleRepository;
import sn.edu.ugb.user.service.dto.ProfilUtilisateurDTO;
import sn.edu.ugb.user.service.mapper.ProfilUtilisateurMapper;

/**
 * Integration tests for the {@link ProfilUtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfilUtilisateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profil-utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static AtomicLong idCounter = new AtomicLong();

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfilUtilisateurRepository profilUtilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProfilUtilisateurMapper profilUtilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilUtilisateurMockMvc;

    private ProfilUtilisateur profilUtilisateur;
    private Role role;

    /**
     * Create an entity for this test.
     */
    public static ProfilUtilisateur createEntity(EntityManager em) {
        Role role = new Role()
            .nom("ROLE_ADMIN")
            .description("Administrateur");
        em.persist(role);
        em.flush();

        return new ProfilUtilisateur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .role(role);
    }

    /**
     * Create an updated entity for this test.
     */
    public static ProfilUtilisateur createUpdatedEntity(EntityManager em) {
        Role role = new Role()
            .nom("ROLE_ENSEIGNANT")
            .description("Enseignant");
        em.persist(role);
        em.flush();

        return new ProfilUtilisateur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .role(role);
    }

    @BeforeEach
    public void initTest() {
        role = new Role()
            .nom("ROLE_ADMIN")
            .description("Administrateur");
        roleRepository.saveAndFlush(role);

        profilUtilisateur = new ProfilUtilisateur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .role(role);
    }

    @Test
    @Transactional
    void createProfilUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = profilUtilisateurRepository.count();

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.role.id").value(role.getId().intValue()));

        // Validate the database
        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    void createProfilUtilisateurWithExistingId() throws Exception {
        profilUtilisateur.setId(1L);
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        long databaseSizeBeforeCreate = profilUtilisateurRepository.count();

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = profilUtilisateurRepository.count();
        profilUtilisateur.setNom(null);

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = profilUtilisateurRepository.count();
        profilUtilisateur.setPrenom(null);

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = profilUtilisateurRepository.count();
        profilUtilisateur.setEmail(null);

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = profilUtilisateurRepository.count();
        profilUtilisateur.setRole(null);

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfilUtilisateurs() throws Exception {
        // Initialize the database
        profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].role.id").value(hasItem(role.getId().intValue())));
    }

    @Test
    @Transactional
    void getProfilUtilisateur() throws Exception {
        // Initialize the database
        profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, profilUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profilUtilisateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.role.id").value(role.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProfilUtilisateur() throws Exception {
        restProfilUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfilUtilisateur() throws Exception {
        // Initialize the database
        profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();

        // Update the profilUtilisateur
        ProfilUtilisateur updatedProfilUtilisateur = profilUtilisateurRepository.findById(profilUtilisateur.getId()).get();
        em.detach(updatedProfilUtilisateur);

        Role updatedRole = new Role()
            .nom("ROLE_ENSEIGNANT")
            .description("Enseignant");
        roleRepository.saveAndFlush(updatedRole);

        updatedProfilUtilisateur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .role(updatedRole);

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(updatedProfilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfilUtilisateur in the database
        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
        ProfilUtilisateur persistedProfilUtilisateur = profilUtilisateurRepository.findById(profilUtilisateur.getId()).get();
        assertThat(persistedProfilUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(persistedProfilUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(persistedProfilUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(persistedProfilUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(persistedProfilUtilisateur.getRole().getId()).isEqualTo(updatedRole.getId());
    }

    @Test
    @Transactional
    void putNonExistingProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();
        profilUtilisateur.setId(idCounter.incrementAndGet());

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();
        profilUtilisateur.setId(idCounter.incrementAndGet());

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idCounter.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();
        profilUtilisateur.setId(idCounter.incrementAndGet());

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfilUtilisateurWithPatch() throws Exception {
        // Initialize the database
        profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();

        ProfilUtilisateur partialUpdatedProfilUtilisateur = new ProfilUtilisateur();
        partialUpdatedProfilUtilisateur.setId(profilUtilisateur.getId());

        partialUpdatedProfilUtilisateur.nom(UPDATED_NOM).email(UPDATED_EMAIL).telephone(UPDATED_TELEPHONE);

        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfilUtilisateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfilUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the database
        ProfilUtilisateur updatedProfilUtilisateur = profilUtilisateurRepository.findById(profilUtilisateur.getId()).get();
        assertThat(updatedProfilUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(updatedProfilUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(updatedProfilUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(updatedProfilUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(updatedProfilUtilisateur.getRole().getId()).isEqualTo(role.getId());
    }

    @Test
    @Transactional
    void fullUpdateProfilUtilisateurWithPatch() throws Exception {
        // Initialize the database
        profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();

        ProfilUtilisateur partialUpdatedProfilUtilisateur = new ProfilUtilisateur();
        partialUpdatedProfilUtilisateur.setId(profilUtilisateur.getId());

        Role updatedRole = new Role()
            .nom("ROLE_ENSEIGNANT")
            .description("Enseignant");
        roleRepository.saveAndFlush(updatedRole);

        partialUpdatedProfilUtilisateur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .role(updatedRole);

        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfilUtilisateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfilUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the database
        ProfilUtilisateur updatedProfilUtilisateur = profilUtilisateurRepository.findById(profilUtilisateur.getId()).get();
        assertThat(updatedProfilUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(updatedProfilUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(updatedProfilUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(updatedProfilUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(updatedProfilUtilisateur.getRole().getId()).isEqualTo(updatedRole.getId());
    }

    @Test
    @Transactional
    void patchNonExistingProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();
        profilUtilisateur.setId(idCounter.incrementAndGet());

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profilUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();
        profilUtilisateur.setId(idCounter.incrementAndGet());

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, idCounter.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = profilUtilisateurRepository.count();
        profilUtilisateur.setId(idCounter.incrementAndGet());

        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfilUtilisateur() throws Exception {
        // Initialize the database
        profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeDelete = profilUtilisateurRepository.count();

        restProfilUtilisateurMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, profilUtilisateur.getId())
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        assertThat(profilUtilisateurRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
    }
}
