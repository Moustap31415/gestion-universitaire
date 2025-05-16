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
import sn.edu.ugb.curriculum.repository.MatiereRepository;
import sn.edu.ugb.curriculum.service.MatiereService;
import sn.edu.ugb.curriculum.service.dto.MatiereDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.Matiere}.
 */
@RestController
@RequestMapping("/api/matieres")
@Tag(name = "Matière", description = "Gestion des matières (cours)")
public class MatiereResource {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereResource.class);

    private static final String ENTITY_NAME = "curriculumServiceMatiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatiereService matiereService;

    private final MatiereRepository matiereRepository;

    public MatiereResource(MatiereService matiereService, MatiereRepository matiereRepository) {
        this.matiereService = matiereService;
        this.matiereRepository = matiereRepository;
    }

    /**
     * {@code POST  /matieres} : Create a new matiere.
     *
     * @param matiereDTO the matiereDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matiereDTO, or with status {@code 400 (Bad Request)} if the matiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Créer une nouvelle matière",
        description = "Enregistre une nouvelle matière (cours) dans le système",
        responses = {
            @ApiResponse(responseCode = "201", description = "Matière créée avec succès", content = @Content(schema = @Schema(implementation = MatiereDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide (ex: ID déjà existant)"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PostMapping("")
    public ResponseEntity<MatiereDTO> createMatiere(@Valid @RequestBody MatiereDTO matiereDTO) throws URISyntaxException {
        LOG.debug("REST request to save Matiere : {}", matiereDTO);
        if (matiereDTO.getId() != null) {
            throw new BadRequestAlertException("A new matiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        matiereDTO = matiereService.save(matiereDTO);
        return ResponseEntity.created(new URI("/api/matieres/" + matiereDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, matiereDTO.getId().toString()))
            .body(matiereDTO);
    }

    /**
     * {@code PUT  /matieres/:id} : Updates an existing matiere.
     *
     * @param id the id of the matiereDTO to save.
     * @param matiereDTO the matiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matiereDTO,
     * or with status {@code 400 (Bad Request)} if the matiereDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Mettre à jour une matière existante",
        description = "Met à jour toutes les propriétés d'une matière existante",
        responses = {
            @ApiResponse(responseCode = "200", description = "Matière mise à jour avec succès", content = @Content(schema = @Schema(implementation = MatiereDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Matière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MatiereDTO> updateMatiere(
        @Parameter(description = "ID de la matière à mettre à jour", required = true) @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatiereDTO matiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Matiere : {}, {}", id, matiereDTO);
        if (matiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        matiereDTO = matiereService.update(matiereDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matiereDTO.getId().toString()))
            .body(matiereDTO);
    }

    /**
     * {@code PATCH  /matieres/:id} : Partial updates given fields of an existing matiere, field will ignore if it is null
     *
     * @param id the id of the matiereDTO to save.
     * @param matiereDTO the matiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matiereDTO,
     * or with status {@code 400 (Bad Request)} if the matiereDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matiereDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Mise à jour partielle d'une matière",
        description = "Met à jour uniquement les champs spécifiés d'une matière existante",
        responses = {
            @ApiResponse(responseCode = "200", description = "Matière partiellement mise à jour", content = @Content(schema = @Schema(implementation = MatiereDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Matière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatiereDTO> partialUpdateMatiere(
        @Parameter(description = "ID de la matière à mettre à jour partiellement", required = true) @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatiereDTO matiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Matiere partially : {}, {}", id, matiereDTO);
        if (matiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatiereDTO> result = matiereService.partialUpdate(matiereDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matiereDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /matieres} : get all the matieres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matieres in body.
     */
    @Operation(
        summary = "Lister toutes les matières",
        description = "Retourne une liste paginée de toutes les matières (cours)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des matières récupérée avec succès", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @GetMapping("")
    public ResponseEntity<List<MatiereDTO>> getAllMatieres(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Matieres");
        Page<MatiereDTO> page = matiereService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matieres/:id} : get the "id" matiere.
     *
     * @param id the id of the matiereDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matiereDTO, or with status {@code 404 (Not Found)}.
     */
    @Operation(
        summary = "Obtenir une matière par son ID",
        description = "Retourne les détails d'une matière spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Matière trouvée", content = @Content(schema = @Schema(implementation = MatiereDTO.class))),
            @ApiResponse(responseCode = "404", description = "Matière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MatiereDTO> getMatiere(
        @Parameter(description = "ID de la matière à récupérer", required = true) @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get Matiere : {}", id);
        Optional<MatiereDTO> matiereDTO = matiereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matiereDTO);
    }

    /**
     * {@code DELETE  /matieres/:id} : delete the "id" matiere.
     *
     * @param id the id of the matiereDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(
        summary = "Supprimer une matière",
        description = "Supprime une matière spécifique de la base de données",
        responses = {
            @ApiResponse(responseCode = "204", description = "Matière supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Matière non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(
        @Parameter(description = "ID de la matière à supprimer", required = true) @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete Matiere : {}", id);
        matiereService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
