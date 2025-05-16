package sn.edu.ugb.student.web.rest;

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
import sn.edu.ugb.student.repository.EtudiantRepository;
import sn.edu.ugb.student.service.EtudiantService;
import sn.edu.ugb.student.service.dto.EtudiantDTO;
import sn.edu.ugb.student.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing {@link sn.edu.ugb.student.domain.Etudiant}.
 */
@RestController
@RequestMapping("/api/etudiants")
@Tag(name = "Etudiant", description = "Gestion des étudiants")
public class EtudiantResource {

    private static final Logger LOG = LoggerFactory.getLogger(EtudiantResource.class);

    private static final String ENTITY_NAME = "studentServiceEtudiant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtudiantService etudiantService;

    private final EtudiantRepository etudiantRepository;

    public EtudiantResource(EtudiantService etudiantService, EtudiantRepository etudiantRepository) {
        this.etudiantService = etudiantService;
        this.etudiantRepository = etudiantRepository;
    }

    /**
     * {@code POST  /etudiants} : Create a new etudiant.
     *
     * @param etudiantDTO the etudiantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etudiantDTO, or with status {@code 400 (Bad Request)} if the etudiant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(
        summary = "Créer un nouvel étudiant",
        description = "Enregistre un nouvel étudiant dans le système",
        responses = {
            @ApiResponse(responseCode = "201", description = "Étudiant créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<EtudiantDTO> createEtudiant(
        @Parameter(description = "DTO de l'étudiant à créer", required = true)
        @Valid @RequestBody EtudiantDTO etudiantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save Etudiant : {}", etudiantDTO);
        if (etudiantDTO.getId() != null) {
            throw new BadRequestAlertException("A new etudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        etudiantDTO = etudiantService.save(etudiantDTO);
        return ResponseEntity.created(new URI("/api/etudiants/" + etudiantDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, etudiantDTO.getId().toString()))
            .body(etudiantDTO);
    }

    /**
     * {@code PUT  /etudiants/:id} : Updates an existing etudiant.
     *
     * @param id the id of the etudiantDTO to save.
     * @param etudiantDTO the etudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etudiantDTO,
     * or with status {@code 400 (Bad Request)} if the etudiantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un étudiant",
        description = "Met à jour toutes les informations d'un étudiant existant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Étudiant mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<EtudiantDTO> updateEtudiant(
        @Parameter(description = "ID de l'étudiant à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO de l'étudiant avec les nouvelles valeurs", required = true)
        @Valid @RequestBody EtudiantDTO etudiantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Etudiant : {}, {}", id, etudiantDTO);
        if (etudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        etudiantDTO = etudiantService.update(etudiantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etudiantDTO.getId().toString()))
            .body(etudiantDTO);
    }

    /**
     * {@code PATCH  /etudiants/:id} : Partial updates given fields of an existing etudiant, field will ignore if it is null
     *
     * @param id the id of the etudiantDTO to save.
     * @param etudiantDTO the etudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etudiantDTO,
     * or with status {@code 400 (Bad Request)} if the etudiantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the etudiantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the etudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        summary = "Mise à jour partielle d'un étudiant",
        description = "Met à jour seulement les champs spécifiés d'un étudiant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Étudiant partiellement mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<EtudiantDTO> partialUpdateEtudiant(
        @Parameter(description = "ID de l'étudiant à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO partiel de l'étudiant avec les champs à mettre à jour", required = true)
        @NotNull @RequestBody EtudiantDTO etudiantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Etudiant partially : {}, {}", id, etudiantDTO);
        if (etudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EtudiantDTO> result = etudiantService.partialUpdate(etudiantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etudiantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /etudiants} : get all the etudiants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etudiants in body.
     */
    @GetMapping("")
    @Operation(
        summary = "Lister tous les étudiants",
        description = "Retourne une liste paginée de tous les étudiants",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des étudiants récupérée"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<List<EtudiantDTO>> getAllEtudiants(
        @Parameter(description = "Paramètres de pagination (page, size, sort)")
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Etudiants");
        Page<EtudiantDTO> page = etudiantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etudiants/:id} : get the "id" etudiant.
     *
     * @param id the id of the etudiantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etudiantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtenir un étudiant par ID",
        description = "Retourne les détails d'un étudiant spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Étudiant trouvé"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
        }
    )
    public ResponseEntity<EtudiantDTO> getEtudiant(
        @Parameter(description = "ID de l'étudiant à récupérer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get Etudiant : {}", id);
        Optional<EtudiantDTO> etudiantDTO = etudiantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etudiantDTO);
    }

    /**
     * {@code DELETE  /etudiants/:id} : delete the "id" etudiant.
     *
     * @param id the id of the etudiantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprimer un étudiant",
        description = "Supprime définitivement un étudiant du système",
        responses = {
            @ApiResponse(responseCode = "204", description = "Étudiant supprimé"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
        }
    )
    public ResponseEntity<Void> deleteEtudiant(
        @Parameter(description = "ID de l'étudiant à supprimer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete Etudiant : {}", id);
        etudiantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
