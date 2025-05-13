package sn.edu.ugb.curriculum.web.rest;

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
import sn.edu.ugb.curriculum.repository.UniteEnseignementRepository;
import sn.edu.ugb.curriculum.service.UniteEnseignementService;
import sn.edu.ugb.curriculum.service.dto.UniteEnseignementDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.UniteEnseignement}.
 */
@RestController
@RequestMapping("/api/unite-enseignements")
public class UniteEnseignementResource {

    private static final Logger LOG = LoggerFactory.getLogger(UniteEnseignementResource.class);

    private static final String ENTITY_NAME = "curriculumServiceUniteEnseignement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniteEnseignementService uniteEnseignementService;

    private final UniteEnseignementRepository uniteEnseignementRepository;

    public UniteEnseignementResource(
        UniteEnseignementService uniteEnseignementService,
        UniteEnseignementRepository uniteEnseignementRepository
    ) {
        this.uniteEnseignementService = uniteEnseignementService;
        this.uniteEnseignementRepository = uniteEnseignementRepository;
    }

    /**
     * {@code POST  /unite-enseignements} : Create a new uniteEnseignement.
     *
     * @param uniteEnseignementDTO the uniteEnseignementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uniteEnseignementDTO, or with status {@code 400 (Bad Request)} if the uniteEnseignement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UniteEnseignementDTO> createUniteEnseignement(@Valid @RequestBody UniteEnseignementDTO uniteEnseignementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UniteEnseignement : {}", uniteEnseignementDTO);
        if (uniteEnseignementDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniteEnseignement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniteEnseignementDTO = uniteEnseignementService.save(uniteEnseignementDTO);
        return ResponseEntity.created(new URI("/api/unite-enseignements/" + uniteEnseignementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uniteEnseignementDTO.getId().toString()))
            .body(uniteEnseignementDTO);
    }

    /**
     * {@code PUT  /unite-enseignements/:id} : Updates an existing uniteEnseignement.
     *
     * @param id the id of the uniteEnseignementDTO to save.
     * @param uniteEnseignementDTO the uniteEnseignementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniteEnseignementDTO,
     * or with status {@code 400 (Bad Request)} if the uniteEnseignementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uniteEnseignementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UniteEnseignementDTO> updateUniteEnseignement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UniteEnseignementDTO uniteEnseignementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UniteEnseignement : {}, {}", id, uniteEnseignementDTO);
        if (uniteEnseignementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniteEnseignementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uniteEnseignementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uniteEnseignementDTO = uniteEnseignementService.update(uniteEnseignementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uniteEnseignementDTO.getId().toString()))
            .body(uniteEnseignementDTO);
    }

    /**
     * {@code PATCH  /unite-enseignements/:id} : Partial updates given fields of an existing uniteEnseignement, field will ignore if it is null
     *
     * @param id the id of the uniteEnseignementDTO to save.
     * @param uniteEnseignementDTO the uniteEnseignementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniteEnseignementDTO,
     * or with status {@code 400 (Bad Request)} if the uniteEnseignementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uniteEnseignementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uniteEnseignementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UniteEnseignementDTO> partialUpdateUniteEnseignement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UniteEnseignementDTO uniteEnseignementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UniteEnseignement partially : {}, {}", id, uniteEnseignementDTO);
        if (uniteEnseignementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniteEnseignementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uniteEnseignementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UniteEnseignementDTO> result = uniteEnseignementService.partialUpdate(uniteEnseignementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uniteEnseignementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unite-enseignements} : get all the uniteEnseignements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uniteEnseignements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UniteEnseignementDTO>> getAllUniteEnseignements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of UniteEnseignements");
        Page<UniteEnseignementDTO> page = uniteEnseignementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unite-enseignements/:id} : get the "id" uniteEnseignement.
     *
     * @param id the id of the uniteEnseignementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uniteEnseignementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UniteEnseignementDTO> getUniteEnseignement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UniteEnseignement : {}", id);
        Optional<UniteEnseignementDTO> uniteEnseignementDTO = uniteEnseignementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniteEnseignementDTO);
    }

    /**
     * {@code DELETE  /unite-enseignements/:id} : delete the "id" uniteEnseignement.
     *
     * @param id the id of the uniteEnseignementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniteEnseignement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UniteEnseignement : {}", id);
        uniteEnseignementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
