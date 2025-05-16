package sn.edu.ugb.user.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.edu.ugb.user.repository.ProfilUtilisateurRepository;
import sn.edu.ugb.user.service.ProfilUtilisateurService;
import sn.edu.ugb.user.service.dto.ProfilUtilisateurDTO;
import sn.edu.ugb.user.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing {@link sn.edu.ugb.user.domain.ProfilUtilisateur}.
 */
@RestController
@RequestMapping("/api/profil-utilisateurs")
@Tag(name = "Profil Utilisateur", description = "Gestion des profils utilisateurs")
public class ProfilUtilisateurResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilUtilisateurResource.class);

    private static final String ENTITY_NAME = "userServiceProfilUtilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilUtilisateurService profilUtilisateurService;

    private final ProfilUtilisateurRepository profilUtilisateurRepository;

    public ProfilUtilisateurResource(
        ProfilUtilisateurService profilUtilisateurService,
        ProfilUtilisateurRepository profilUtilisateurRepository
    ) {
        this.profilUtilisateurService = profilUtilisateurService;
        this.profilUtilisateurRepository = profilUtilisateurRepository;
    }

    /**
     * {@code POST  /profil-utilisateurs} : Create a new profilUtilisateur.
     *
     * @param profilUtilisateurDTO the profilUtilisateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profilUtilisateurDTO, or with status {@code 400 (Bad Request)} if the profilUtilisateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(
        summary = "Créer un nouveau profil utilisateur",
        description = "Crée un nouveau profil utilisateur dans le système",
        responses = {
            @ApiResponse(responseCode = "201", description = "Profil utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Le profil existe déjà ou données incorrectes"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<ProfilUtilisateurDTO> createProfilUtilisateur(
        @Parameter(description = "DTO du profil utilisateur à créer", required = true)
        @Valid @RequestBody ProfilUtilisateurDTO profilUtilisateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfilUtilisateur : {}", profilUtilisateurDTO);
        if (profilUtilisateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new profilUtilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        profilUtilisateurDTO = profilUtilisateurService.save(profilUtilisateurDTO);
        return ResponseEntity.created(new URI("/api/profil-utilisateurs/" + profilUtilisateurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, profilUtilisateurDTO.getId().toString()))
            .body(profilUtilisateurDTO);
    }

    /**
     * {@code PUT  /profil-utilisateurs/:id} : Updates an existing profilUtilisateur.
     *
     * @param id the id of the profilUtilisateurDTO to save.
     * @param profilUtilisateurDTO the profilUtilisateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilUtilisateurDTO,
     * or with status {@code 400 (Bad Request)} if the profilUtilisateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profilUtilisateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un profil utilisateur",
        description = "Met à jour complètement un profil utilisateur existant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profil utilisateur mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "ID invalide ou données incorrectes"),
            @ApiResponse(responseCode = "404", description = "Profil utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<ProfilUtilisateurDTO> updateProfilUtilisateur(
        @Parameter(description = "ID du profil utilisateur à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO du profil utilisateur avec les nouvelles valeurs", required = true)
        @Valid @RequestBody ProfilUtilisateurDTO profilUtilisateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfilUtilisateur : {}, {}", id, profilUtilisateurDTO);
        if (profilUtilisateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profilUtilisateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profilUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        profilUtilisateurDTO = profilUtilisateurService.update(profilUtilisateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profilUtilisateurDTO.getId().toString()))
            .body(profilUtilisateurDTO);
    }

    /**
     * {@code PATCH  /profil-utilisateurs/:id} : Partial updates given fields of an existing profilUtilisateur, field will ignore if it is null
     *
     * @param id the id of the profilUtilisateurDTO to save.
     * @param profilUtilisateurDTO the profilUtilisateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilUtilisateurDTO,
     * or with status {@code 400 (Bad Request)} if the profilUtilisateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profilUtilisateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profilUtilisateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        summary = "Mise à jour partielle d'un profil utilisateur",
        description = "Met à jour partiellement un profil utilisateur (seuls les champs non nuls seront modifiés)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profil utilisateur mis à jour partiellement avec succès"),
            @ApiResponse(responseCode = "400", description = "ID invalide ou données incorrectes"),
            @ApiResponse(responseCode = "404", description = "Profil utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<ProfilUtilisateurDTO> partialUpdateProfilUtilisateur(
        @Parameter(description = "ID du profil utilisateur à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO partiel du profil utilisateur avec les champs à mettre à jour", required = true)
        @NotNull @RequestBody ProfilUtilisateurDTO profilUtilisateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfilUtilisateur partially : {}, {}", id, profilUtilisateurDTO);
        if (profilUtilisateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profilUtilisateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profilUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfilUtilisateurDTO> result = profilUtilisateurService.partialUpdate(profilUtilisateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profilUtilisateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /profil-utilisateurs} : get all the profilUtilisateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profilUtilisateurs in body.
     */
    @GetMapping("")
    @Operation(
        summary = "Lister tous les profils utilisateurs",
        description = "Retourne une liste paginée de tous les profils utilisateurs",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des profils utilisateurs récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<List<ProfilUtilisateurDTO>> getAllProfilUtilisateurs(
        @Parameter(description = "Paramètres de pagination (page, size, sort)")
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ProfilUtilisateurs");
        Page<ProfilUtilisateurDTO> page = profilUtilisateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /profil-utilisateurs/:id} : get the "id" profilUtilisateur.
     *
     * @param id the id of the profilUtilisateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profilUtilisateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtenir un profil utilisateur par ID",
        description = "Retourne les détails d'un profil utilisateur spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profil utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Profil utilisateur non trouvé")
        }
    )
    public ResponseEntity<ProfilUtilisateurDTO> getProfilUtilisateur(
        @Parameter(description = "ID du profil utilisateur à récupérer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get ProfilUtilisateur : {}", id);
        Optional<ProfilUtilisateurDTO> profilUtilisateurDTO = profilUtilisateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profilUtilisateurDTO);
    }

    /**
     * {@code DELETE  /profil-utilisateurs/:id} : delete the "id" profilUtilisateur.
     *
     * @param id the id of the profilUtilisateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprimer un profil utilisateur",
        description = "Supprime définitivement un profil utilisateur du système",
        responses = {
            @ApiResponse(responseCode = "204", description = "Profil utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Profil utilisateur non trouvé")
        }
    )
    public ResponseEntity<Void> deleteProfilUtilisateur(
        @Parameter(description = "ID du profil utilisateur à supprimer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete ProfilUtilisateur : {}", id);
        profilUtilisateurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
