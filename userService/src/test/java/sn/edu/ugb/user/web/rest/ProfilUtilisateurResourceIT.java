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
import sn.edu.ugb.user.IntegrationTest;
import sn.edu.ugb.user.domain.ProfilUtilisateur;
import sn.edu.ugb.user.repository.ProfilUtilisateurRepository;
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

    private static final Long DEFAULT_ROLE_ID = 1L;
    private static final Long UPDATED_ROLE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/profil-utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfilUtilisateurRepository profilUtilisateurRepository;

    @Autowired
    private ProfilUtilisateurMapper profilUtilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilUtilisateurMockMvc;

    private ProfilUtilisateur profilUtilisateur;

    private ProfilUtilisateur insertedProfilUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilUtilisateur createEntity() {
        return new ProfilUtilisateur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .roleId(DEFAULT_ROLE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilUtilisateur createUpdatedEntity() {
        return new ProfilUtilisateur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .roleId(UPDATED_ROLE_ID);
    }

    @BeforeEach
    void initTest() {
        profilUtilisateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfilUtilisateur != null) {
            profilUtilisateurRepository.delete(insertedProfilUtilisateur);
            insertedProfilUtilisateur = null;
        }
    }

    @Test
    @Transactional
    void createProfilUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);
        var returnedProfilUtilisateurDTO = om.readValue(
            restProfilUtilisateurMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(profilUtilisateurDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfilUtilisateurDTO.class
        );

        // Validate the ProfilUtilisateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfilUtilisateur = profilUtilisateurMapper.toEntity(returnedProfilUtilisateurDTO);
        assertProfilUtilisateurUpdatableFieldsEquals(returnedProfilUtilisateur, getPersistedProfilUtilisateur(returnedProfilUtilisateur));

        insertedProfilUtilisateur = returnedProfilUtilisateur;
    }

    @Test
    @Transactional
    void createProfilUtilisateurWithExistingId() throws Exception {
        // Create the ProfilUtilisateur with an existing ID
        profilUtilisateur.setId(1L);
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profilUtilisateur.setNom(null);

        // Create the ProfilUtilisateur, which fails.
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profilUtilisateur.setPrenom(null);

        // Create the ProfilUtilisateur, which fails.
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profilUtilisateur.setEmail(null);

        // Create the ProfilUtilisateur, which fails.
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profilUtilisateur.setRoleId(null);

        // Create the ProfilUtilisateur, which fails.
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        restProfilUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfilUtilisateurs() throws Exception {
        // Initialize the database
        insertedProfilUtilisateur = profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        // Get all the profilUtilisateurList
        restProfilUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID.intValue())));
    }

    @Test
    @Transactional
    void getProfilUtilisateur() throws Exception {
        // Initialize the database
        insertedProfilUtilisateur = profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        // Get the profilUtilisateur
        restProfilUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, profilUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profilUtilisateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProfilUtilisateur() throws Exception {
        // Get the profilUtilisateur
        restProfilUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfilUtilisateur() throws Exception {
        // Initialize the database
        insertedProfilUtilisateur = profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profilUtilisateur
        ProfilUtilisateur updatedProfilUtilisateur = profilUtilisateurRepository.findById(profilUtilisateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfilUtilisateur are not directly saved in db
        em.detach(updatedProfilUtilisateur);
        updatedProfilUtilisateur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .roleId(UPDATED_ROLE_ID);
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
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfilUtilisateurToMatchAllProperties(updatedProfilUtilisateur);
    }

    @Test
    @Transactional
    void putNonExistingProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profilUtilisateur.setId(longCount.incrementAndGet());

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profilUtilisateur.setId(longCount.incrementAndGet());

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profilUtilisateur.setId(longCount.incrementAndGet());

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfilUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedProfilUtilisateur = profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profilUtilisateur using partial update
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

        // Validate the ProfilUtilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfilUtilisateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfilUtilisateur, profilUtilisateur),
            getPersistedProfilUtilisateur(profilUtilisateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfilUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedProfilUtilisateur = profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profilUtilisateur using partial update
        ProfilUtilisateur partialUpdatedProfilUtilisateur = new ProfilUtilisateur();
        partialUpdatedProfilUtilisateur.setId(profilUtilisateur.getId());

        partialUpdatedProfilUtilisateur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .roleId(UPDATED_ROLE_ID);

        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfilUtilisateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfilUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the ProfilUtilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfilUtilisateurUpdatableFieldsEquals(
            partialUpdatedProfilUtilisateur,
            getPersistedProfilUtilisateur(partialUpdatedProfilUtilisateur)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profilUtilisateur.setId(longCount.incrementAndGet());

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profilUtilisateurDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profilUtilisateur.setId(longCount.incrementAndGet());

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfilUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profilUtilisateur.setId(longCount.incrementAndGet());

        // Create the ProfilUtilisateur
        ProfilUtilisateurDTO profilUtilisateurDTO = profilUtilisateurMapper.toDto(profilUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilUtilisateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfilUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfilUtilisateur() throws Exception {
        // Initialize the database
        insertedProfilUtilisateur = profilUtilisateurRepository.saveAndFlush(profilUtilisateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profilUtilisateur
        restProfilUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, profilUtilisateur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profilUtilisateurRepository.count();
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

    protected ProfilUtilisateur getPersistedProfilUtilisateur(ProfilUtilisateur profilUtilisateur) {
        return profilUtilisateurRepository.findById(profilUtilisateur.getId()).orElseThrow();
    }

    protected void assertPersistedProfilUtilisateurToMatchAllProperties(ProfilUtilisateur expectedProfilUtilisateur) {
        assertProfilUtilisateurAllPropertiesEquals(expectedProfilUtilisateur, getPersistedProfilUtilisateur(expectedProfilUtilisateur));
    }

    protected void assertPersistedProfilUtilisateurToMatchUpdatableProperties(ProfilUtilisateur expectedProfilUtilisateur) {
        assertProfilUtilisateurAllUpdatablePropertiesEquals(
            expectedProfilUtilisateur,
            getPersistedProfilUtilisateur(expectedProfilUtilisateur)
        );
    }
}
