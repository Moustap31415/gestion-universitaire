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
import sn.edu.ugb.student.repository.InscriptionRepository;
import sn.edu.ugb.student.service.InscriptionService;
import sn.edu.ugb.student.service.dto.InscriptionDTO;
import sn.edu.ugb.student.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing {@link sn.edu.ugb.student.domain.Inscription}.
 */
@RestController
@RequestMapping("/api/inscriptions")
@Tag(name = "Inscription", description = "Gestion des inscriptions des étudiants")
public class InscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(InscriptionResource.class);

    private static final String ENTITY_NAME = "studentServiceInscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionService inscriptionService;

    private final InscriptionRepository inscriptionRepository;

    public InscriptionResource(InscriptionService inscriptionService, InscriptionRepository inscriptionRepository) {
        this.inscriptionService = inscriptionService;
        this.inscriptionRepository = inscriptionRepository;
    }

    /**
     * {@code POST  /inscriptions} : Create a new inscription.
     *
     * @param inscriptionDTO the inscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionDTO, or with status {@code 400 (Bad Request)} if the inscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(
        summary = "Créer une nouvelle inscription",
        description = "Enregistre une nouvelle inscription pour un étudiant",
        responses = {
            @ApiResponse(responseCode = "201", description = "Inscription créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<InscriptionDTO> createInscription(
        @Parameter(description = "DTO de l'inscription à créer", required = true)
        @Valid @RequestBody InscriptionDTO inscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inscriptionDTO = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.created(new URI("/api/inscriptions/" + inscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString()))
            .body(inscriptionDTO);
    }

    /**
     * {@code PUT  /inscriptions/:id} : Updates an existing inscription.
     *
     * @param id the id of the inscriptionDTO to save.
     * @param inscriptionDTO the inscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour une inscription",
        description = "Met à jour toutes les informations d'une inscription existante",
        responses = {
            @ApiResponse(responseCode = "200", description = "Inscription mise à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<InscriptionDTO> updateInscription(
        @Parameter(description = "ID de l'inscription à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO de l'inscription avec les nouvelles valeurs", required = true)
        @Valid @RequestBody InscriptionDTO inscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Inscription : {}, {}", id, inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inscriptionDTO = inscriptionService.update(inscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString()))
            .body(inscriptionDTO);
    }

    /**
     * {@code PATCH  /inscriptions/:id} : Partial updates given fields of an existing inscription, field will ignore if it is null
     *
     * @param id the id of the inscriptionDTO to save.
     * @param inscriptionDTO the inscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        summary = "Mise à jour partielle d'une inscription",
        description = "Met à jour seulement les champs spécifiés d'une inscription",
        responses = {
            @ApiResponse(responseCode = "200", description = "Inscription partiellement mise à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<InscriptionDTO> partialUpdateInscription(
        @Parameter(description = "ID de l'inscription à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO partiel de l'inscription avec les champs à mettre à jour", required = true)
        @NotNull @RequestBody InscriptionDTO inscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Inscription partially : {}, {}", id, inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InscriptionDTO> result = inscriptionService.partialUpdate(inscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inscriptions} : get all the inscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptions in body.
     */
    @GetMapping("")
    @Operation(
        summary = "Lister toutes les inscriptions",
        description = "Retourne une liste paginée de toutes les inscriptions",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des inscriptions récupérée"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<List<InscriptionDTO>> getAllInscriptions(
        @Parameter(description = "Paramètres de pagination (page, size, sort)")
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Inscriptions");
        Page<InscriptionDTO> page = inscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscriptions/:id} : get the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtenir une inscription par ID",
        description = "Retourne les détails d'une inscription spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Inscription trouvée"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée")
        }
    )
    public ResponseEntity<InscriptionDTO> getInscription(
        @Parameter(description = "ID de l'inscription à récupérer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get Inscription : {}", id);
        Optional<InscriptionDTO> inscriptionDTO = inscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionDTO);
    }

    /**
     * {@code DELETE  /inscriptions/:id} : delete the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprimer une inscription",
        description = "Supprime définitivement une inscription",
        responses = {
            @ApiResponse(responseCode = "204", description = "Inscription supprimée"),
            @ApiResponse(responseCode = "404", description = "Inscription non trouvée")
        }
    )
    public ResponseEntity<Void> deleteInscription(
        @Parameter(description = "ID de l'inscription à supprimer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete Inscription : {}", id);
        inscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
