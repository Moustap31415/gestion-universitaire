package sn.edu.ugb.teacher.web.rest;

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
import sn.edu.ugb.teacher.repository.AffectationEnseignementRepository;
import sn.edu.ugb.teacher.service.AffectationEnseignementService;
import sn.edu.ugb.teacher.service.dto.AffectationEnseignementDTO;
import sn.edu.ugb.teacher.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.teacher.domain.AffectationEnseignement}.
 */
@RestController
@RequestMapping("/api/affectation-enseignements")
public class AffectationEnseignementResource {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationEnseignementResource.class);

    private static final String ENTITY_NAME = "teacherServiceAffectationEnseignement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffectationEnseignementService affectationEnseignementService;

    private final AffectationEnseignementRepository affectationEnseignementRepository;

    public AffectationEnseignementResource(
        AffectationEnseignementService affectationEnseignementService,
        AffectationEnseignementRepository affectationEnseignementRepository
    ) {
        this.affectationEnseignementService = affectationEnseignementService;
        this.affectationEnseignementRepository = affectationEnseignementRepository;
    }

    /**
     * {@code POST  /affectation-enseignements} : Create a new affectationEnseignement.
     *
     * @param affectationEnseignementDTO the affectationEnseignementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectationEnseignementDTO, or with status {@code 400 (Bad Request)} if the affectationEnseignement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AffectationEnseignementDTO> createAffectationEnseignement(
        @Valid @RequestBody AffectationEnseignementDTO affectationEnseignementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AffectationEnseignement : {}", affectationEnseignementDTO);
        if (affectationEnseignementDTO.getId() != null) {
            throw new BadRequestAlertException("A new affectationEnseignement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        affectationEnseignementDTO = affectationEnseignementService.save(affectationEnseignementDTO);
        return ResponseEntity.created(new URI("/api/affectation-enseignements/" + affectationEnseignementDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, affectationEnseignementDTO.getId().toString())
            )
            .body(affectationEnseignementDTO);
    }

    /**
     * {@code PUT  /affectation-enseignements/:id} : Updates an existing affectationEnseignement.
     *
     * @param id the id of the affectationEnseignementDTO to save.
     * @param affectationEnseignementDTO the affectationEnseignementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationEnseignementDTO,
     * or with status {@code 400 (Bad Request)} if the affectationEnseignementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectationEnseignementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AffectationEnseignementDTO> updateAffectationEnseignement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AffectationEnseignementDTO affectationEnseignementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AffectationEnseignement : {}, {}", id, affectationEnseignementDTO);
        if (affectationEnseignementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationEnseignementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationEnseignementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        affectationEnseignementDTO = affectationEnseignementService.update(affectationEnseignementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationEnseignementDTO.getId().toString()))
            .body(affectationEnseignementDTO);
    }

    /**
     * {@code PATCH  /affectation-enseignements/:id} : Partial updates given fields of an existing affectationEnseignement, field will ignore if it is null
     *
     * @param id the id of the affectationEnseignementDTO to save.
     * @param affectationEnseignementDTO the affectationEnseignementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationEnseignementDTO,
     * or with status {@code 400 (Bad Request)} if the affectationEnseignementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affectationEnseignementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectationEnseignementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffectationEnseignementDTO> partialUpdateAffectationEnseignement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AffectationEnseignementDTO affectationEnseignementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AffectationEnseignement partially : {}, {}", id, affectationEnseignementDTO);
        if (affectationEnseignementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationEnseignementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationEnseignementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffectationEnseignementDTO> result = affectationEnseignementService.partialUpdate(affectationEnseignementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationEnseignementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affectation-enseignements} : get all the affectationEnseignements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affectationEnseignements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AffectationEnseignementDTO>> getAllAffectationEnseignements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AffectationEnseignements");
        Page<AffectationEnseignementDTO> page = affectationEnseignementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affectation-enseignements/:id} : get the "id" affectationEnseignement.
     *
     * @param id the id of the affectationEnseignementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectationEnseignementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AffectationEnseignementDTO> getAffectationEnseignement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AffectationEnseignement : {}", id);
        Optional<AffectationEnseignementDTO> affectationEnseignementDTO = affectationEnseignementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affectationEnseignementDTO);
    }

    /**
     * {@code DELETE  /affectation-enseignements/:id} : delete the "id" affectationEnseignement.
     *
     * @param id the id of the affectationEnseignementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAffectationEnseignement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AffectationEnseignement : {}", id);
        affectationEnseignementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
