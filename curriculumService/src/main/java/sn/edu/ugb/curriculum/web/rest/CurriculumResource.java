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
import sn.edu.ugb.curriculum.repository.CurriculumRepository;
import sn.edu.ugb.curriculum.service.CurriculumService;
import sn.edu.ugb.curriculum.service.dto.CurriculumDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.Curriculum}.
 */
@RestController
@RequestMapping("/api/curricula")
@Tag(name = "Curriculum", description = "Gestion des curriculums (associations filière-module-semestre)")
public class CurriculumResource {

    private static final Logger LOG = LoggerFactory.getLogger(CurriculumResource.class);

    private static final String ENTITY_NAME = "curriculumServiceCurriculum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurriculumService curriculumService;

    private final CurriculumRepository curriculumRepository;

    public CurriculumResource(CurriculumService curriculumService, CurriculumRepository curriculumRepository) {
        this.curriculumService = curriculumService;
        this.curriculumRepository = curriculumRepository;
    }

    /**
     * {@code POST  /curricula} : Create a new curriculum.
     *
     * @param curriculumDTO the curriculumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new curriculumDTO, or with status {@code 400 (Bad Request)} if the curriculum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Créer un nouveau curriculum",
        description = "Crée une nouvelle association entre une filière, un module et un semestre",
        responses = {
            @ApiResponse(responseCode = "201", description = "Curriculum créé avec succès", content = @Content(schema = @Schema(implementation = CurriculumDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide (ex: ID déjà existant)"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PostMapping("")
    public ResponseEntity<CurriculumDTO> createCurriculum(@Valid @RequestBody CurriculumDTO curriculumDTO) throws URISyntaxException {
        LOG.debug("REST request to save Curriculum : {}", curriculumDTO);
        if (curriculumDTO.getId() != null) {
            throw new BadRequestAlertException("A new curriculum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        curriculumDTO = curriculumService.save(curriculumDTO);
        return ResponseEntity.created(new URI("/api/curricula/" + curriculumDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, curriculumDTO.getId().toString()))
            .body(curriculumDTO);
    }

    /**
     * {@code PUT  /curricula/:id} : Updates an existing curriculum.
     *
     * @param id the id of the curriculumDTO to save.
     * @param curriculumDTO the curriculumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculumDTO,
     * or with status {@code 400 (Bad Request)} if the curriculumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the curriculumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Mettre à jour un curriculum existant",
        description = "Met à jour toutes les propriétés d'un curriculum existant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Curriculum mis à jour avec succès", content = @Content(schema = @Schema(implementation = CurriculumDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Curriculum non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CurriculumDTO> updateCurriculum(
        @Parameter(description = "ID du curriculum à mettre à jour", required = true) @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurriculumDTO curriculumDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Curriculum : {}, {}", id, curriculumDTO);
        if (curriculumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        curriculumDTO = curriculumService.update(curriculumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculumDTO.getId().toString()))
            .body(curriculumDTO);
    }

    /**
     * {@code PATCH  /curricula/:id} : Partial updates given fields of an existing curriculum, field will ignore if it is null
     *
     * @param id the id of the curriculumDTO to save.
     * @param curriculumDTO the curriculumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculumDTO,
     * or with status {@code 400 (Bad Request)} if the curriculumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the curriculumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the curriculumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(
        summary = "Mise à jour partielle d'un curriculum",
        description = "Met à jour uniquement les champs spécifiés d'un curriculum existant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Curriculum partiellement mis à jour", content = @Content(schema = @Schema(implementation = CurriculumDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Curriculum non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurriculumDTO> partialUpdateCurriculum(
        @Parameter(description = "ID du curriculum à mettre à jour partiellement", required = true) @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurriculumDTO curriculumDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Curriculum partially : {}, {}", id, curriculumDTO);
        if (curriculumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurriculumDTO> result = curriculumService.partialUpdate(curriculumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /curricula} : get all the curricula.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of curricula in body.
     */
    @Operation(
        summary = "Lister tous les curriculums",
        description = "Retourne une liste paginée de tous les curriculums",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des curriculums récupérée avec succès", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @GetMapping("")
    public ResponseEntity<List<CurriculumDTO>> getAllCurricula(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Curricula");
        Page<CurriculumDTO> page = curriculumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /curricula/:id} : get the "id" curriculum.
     *
     * @param id the id of the curriculumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the curriculumDTO, or with status {@code 404 (Not Found)}.
     */
    @Operation(
        summary = "Obtenir un curriculum par son ID",
        description = "Retourne les détails d'un curriculum spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Curriculum trouvé", content = @Content(schema = @Schema(implementation = CurriculumDTO.class))),
            @ApiResponse(responseCode = "404", description = "Curriculum non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumDTO> getCurriculum(
        @Parameter(description = "ID du curriculum à récupérer", required = true) @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get Curriculum : {}", id);
        Optional<CurriculumDTO> curriculumDTO = curriculumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(curriculumDTO);
    }

    /**
     * {@code DELETE  /curricula/:id} : delete the "id" curriculum.
     *
     * @param id the id of the curriculumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(
        summary = "Supprimer un curriculum",
        description = "Supprime un curriculum spécifique de la base de données",
        responses = {
            @ApiResponse(responseCode = "204", description = "Curriculum supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Curriculum non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(
        @Parameter(description = "ID du curriculum à supprimer", required = true) @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete Curriculum : {}", id);
        curriculumService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
