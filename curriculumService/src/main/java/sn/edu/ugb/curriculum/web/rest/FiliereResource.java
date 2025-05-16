package sn.edu.ugb.curriculum.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import sn.edu.ugb.curriculum.repository.FiliereRepository;
import sn.edu.ugb.curriculum.service.FiliereService;
import sn.edu.ugb.curriculum.service.dto.FiliereDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.Filiere}.
 */
@RestController
@RequestMapping("/api/filieres")
@Tag(name = "Filière", description = "Gestion des filières de formation")
public class FiliereResource {

    private static final Logger LOG = LoggerFactory.getLogger(FiliereResource.class);

    private static final String ENTITY_NAME = "curriculumServiceFiliere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiliereService filiereService;

    private final FiliereRepository filiereRepository;

    public FiliereResource(FiliereService filiereService, FiliereRepository filiereRepository) {
        this.filiereService = filiereService;
        this.filiereRepository = filiereRepository;
    }

    /**
     * {@code POST  /filieres} : Create a new filiere.
     *
     * @param filiereDTO the filiereDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filiereDTO, or with status {@code 400 (Bad Request)} if the filiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Créer une nouvelle filière",
        description = "Enregistre une nouvelle filière de formation dans le système",
        responses = {
            @ApiResponse(responseCode = "201", description = "Filière créée avec succès", content = @Content(schema = @Schema(implementation = FiliereDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide (ex: ID déjà existant)"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PostMapping("")
    public ResponseEntity<FiliereDTO> createFiliere(@Valid @RequestBody FiliereDTO filiereDTO) throws URISyntaxException {
        LOG.debug("REST request to save Filiere : {}", filiereDTO);
        if (filiereDTO.getId() != null) {
            throw new BadRequestAlertException("A new filiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        filiereDTO = filiereService.save(filiereDTO);
        return ResponseEntity.created(new URI("/api/filieres/" + filiereDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, filiereDTO.getId().toString()))
            .body(filiereDTO);
    }

    /**
     * {@code PUT  /filieres/:id} : Updates an existing filiere.
     *
     * @param id the id of the filiereDTO to save.
     * @param filiereDTO the filiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filiereDTO,
     * or with status {@code 400 (Bad Request)} if the filiereDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Mettre à jour une filière existante",
        description = "Met à jour toutes les propriétés d'une filière existante",
        responses = {
            @ApiResponse(responseCode = "200", description = "Filière mise à jour avec succès", content = @Content(schema = @Schema(implementation = FiliereDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Filière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FiliereDTO> updateFiliere(
        @Parameter(description = "ID de la filière à mettre à jour", required = true) @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiliereDTO filiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Filiere : {}, {}", id, filiereDTO);
        if (filiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        filiereDTO = filiereService.update(filiereDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filiereDTO.getId().toString()))
            .body(filiereDTO);
    }

    /**
     * {@code PATCH  /filieres/:id} : Partial updates given fields of an existing filiere, field will ignore if it is null
     *
     * @param id the id of the filiereDTO to save.
     * @param filiereDTO the filiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filiereDTO,
     * or with status {@code 400 (Bad Request)} if the filiereDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filiereDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Mise à jour partielle d'une filière",
        description = "Met à jour uniquement les champs spécifiés d'une filière existante",
        responses = {
            @ApiResponse(responseCode = "200", description = "Filière partiellement mise à jour", content = @Content(schema = @Schema(implementation = FiliereDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Filière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiliereDTO> partialUpdateFiliere(
        @Parameter(description = "ID de la filière à mettre à jour partiellement", required = true) @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiliereDTO filiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Filiere partially : {}, {}", id, filiereDTO);
        if (filiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiliereDTO> result = filiereService.partialUpdate(filiereDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filiereDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /filieres} : get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filieres in body.
     */
    @Operation(
        summary = "Lister toutes les filières",
        description = "Retourne une liste paginée de toutes les filières de formation",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des filières récupérée avec succès", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @GetMapping("")
    public ResponseEntity<List<FiliereDTO>> getAllFilieres(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Filieres");
        Page<FiliereDTO> page = filiereService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filieres/:id} : get the "id" filiere.
     *
     * @param id the id of the filiereDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filiereDTO, or with status {@code 404 (Not Found)}.
     */
    @Operation(
        summary = "Obtenir une filière par son ID",
        description = "Retourne les détails d'une filière spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Filière trouvée", content = @Content(schema = @Schema(implementation = FiliereDTO.class))),
            @ApiResponse(responseCode = "404", description = "Filière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FiliereDTO> getFiliere(
        @Parameter(description = "ID de la filière à récupérer", required = true) @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get Filiere : {}", id);
        Optional<FiliereDTO> filiereDTO = filiereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filiereDTO);
    }

    /**
     * {@code DELETE  /filieres/:id} : delete the "id" filiere.
     *
     * @param id the id of the filiereDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(
        summary = "Supprimer une filière",
        description = "Supprime une filière spécifique de la base de données",
        responses = {
            @ApiResponse(responseCode = "204", description = "Filière supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Filière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(
        @Parameter(description = "ID de la filière à supprimer", required = true) @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete Filiere : {}", id);
        filiereService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
