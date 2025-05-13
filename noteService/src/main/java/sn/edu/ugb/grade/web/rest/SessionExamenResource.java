package sn.edu.ugb.grade.web.rest;

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
import sn.edu.ugb.grade.repository.SessionExamenRepository;
import sn.edu.ugb.grade.service.SessionExamenService;
import sn.edu.ugb.grade.service.dto.SessionExamenDTO;
import sn.edu.ugb.grade.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.grade.domain.SessionExamen}.
 */
@RestController
@RequestMapping("/api/session-examen")
public class SessionExamenResource {

    private static final Logger LOG = LoggerFactory.getLogger(SessionExamenResource.class);

    private static final String ENTITY_NAME = "noteServiceSessionExamen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionExamenService sessionExamenService;

    private final SessionExamenRepository sessionExamenRepository;

    public SessionExamenResource(SessionExamenService sessionExamenService, SessionExamenRepository sessionExamenRepository) {
        this.sessionExamenService = sessionExamenService;
        this.sessionExamenRepository = sessionExamenRepository;
    }

    /**
     * {@code POST  /session-examen} : Create a new sessionExamen.
     *
     * @param sessionExamenDTO the sessionExamenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionExamenDTO, or with status {@code 400 (Bad Request)} if the sessionExamen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SessionExamenDTO> createSessionExamen(@Valid @RequestBody SessionExamenDTO sessionExamenDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SessionExamen : {}", sessionExamenDTO);
        if (sessionExamenDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionExamen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sessionExamenDTO = sessionExamenService.save(sessionExamenDTO);
        return ResponseEntity.created(new URI("/api/session-examen/" + sessionExamenDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sessionExamenDTO.getId().toString()))
            .body(sessionExamenDTO);
    }

    /**
     * {@code PUT  /session-examen/:id} : Updates an existing sessionExamen.
     *
     * @param id the id of the sessionExamenDTO to save.
     * @param sessionExamenDTO the sessionExamenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionExamenDTO,
     * or with status {@code 400 (Bad Request)} if the sessionExamenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionExamenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SessionExamenDTO> updateSessionExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SessionExamenDTO sessionExamenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SessionExamen : {}, {}", id, sessionExamenDTO);
        if (sessionExamenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionExamenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionExamenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sessionExamenDTO = sessionExamenService.update(sessionExamenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionExamenDTO.getId().toString()))
            .body(sessionExamenDTO);
    }

    /**
     * {@code PATCH  /session-examen/:id} : Partial updates given fields of an existing sessionExamen, field will ignore if it is null
     *
     * @param id the id of the sessionExamenDTO to save.
     * @param sessionExamenDTO the sessionExamenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionExamenDTO,
     * or with status {@code 400 (Bad Request)} if the sessionExamenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionExamenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionExamenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionExamenDTO> partialUpdateSessionExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SessionExamenDTO sessionExamenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SessionExamen partially : {}, {}", id, sessionExamenDTO);
        if (sessionExamenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionExamenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionExamenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionExamenDTO> result = sessionExamenService.partialUpdate(sessionExamenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionExamenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-examen} : get all the sessionExamen.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionExamen in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SessionExamenDTO>> getAllSessionExamen(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SessionExamen");
        Page<SessionExamenDTO> page = sessionExamenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /session-examen/:id} : get the "id" sessionExamen.
     *
     * @param id the id of the sessionExamenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionExamenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionExamenDTO> getSessionExamen(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SessionExamen : {}", id);
        Optional<SessionExamenDTO> sessionExamenDTO = sessionExamenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionExamenDTO);
    }

    /**
     * {@code DELETE  /session-examen/:id} : delete the "id" sessionExamen.
     *
     * @param id the id of the sessionExamenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionExamen(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SessionExamen : {}", id);
        sessionExamenService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
