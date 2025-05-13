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
import sn.edu.ugb.curriculum.repository.SemestreRepository;
import sn.edu.ugb.curriculum.service.SemestreService;
import sn.edu.ugb.curriculum.service.dto.SemestreDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.Semestre}.
 */
@RestController
@RequestMapping("/api/semestres")
public class SemestreResource {

    private static final Logger LOG = LoggerFactory.getLogger(SemestreResource.class);

    private static final String ENTITY_NAME = "curriculumServiceSemestre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemestreService semestreService;

    private final SemestreRepository semestreRepository;

    public SemestreResource(SemestreService semestreService, SemestreRepository semestreRepository) {
        this.semestreService = semestreService;
        this.semestreRepository = semestreRepository;
    }

    /**
     * {@code POST  /semestres} : Create a new semestre.
     *
     * @param semestreDTO the semestreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semestreDTO, or with status {@code 400 (Bad Request)} if the semestre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SemestreDTO> createSemestre(@Valid @RequestBody SemestreDTO semestreDTO) throws URISyntaxException {
        LOG.debug("REST request to save Semestre : {}", semestreDTO);
        if (semestreDTO.getId() != null) {
            throw new BadRequestAlertException("A new semestre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        semestreDTO = semestreService.save(semestreDTO);
        return ResponseEntity.created(new URI("/api/semestres/" + semestreDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, semestreDTO.getId().toString()))
            .body(semestreDTO);
    }

    /**
     * {@code PUT  /semestres/:id} : Updates an existing semestre.
     *
     * @param id the id of the semestreDTO to save.
     * @param semestreDTO the semestreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semestreDTO,
     * or with status {@code 400 (Bad Request)} if the semestreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semestreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SemestreDTO> updateSemestre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SemestreDTO semestreDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Semestre : {}, {}", id, semestreDTO);
        if (semestreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semestreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semestreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        semestreDTO = semestreService.update(semestreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semestreDTO.getId().toString()))
            .body(semestreDTO);
    }

    /**
     * {@code PATCH  /semestres/:id} : Partial updates given fields of an existing semestre, field will ignore if it is null
     *
     * @param id the id of the semestreDTO to save.
     * @param semestreDTO the semestreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semestreDTO,
     * or with status {@code 400 (Bad Request)} if the semestreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the semestreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the semestreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SemestreDTO> partialUpdateSemestre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SemestreDTO semestreDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Semestre partially : {}, {}", id, semestreDTO);
        if (semestreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semestreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semestreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SemestreDTO> result = semestreService.partialUpdate(semestreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semestreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /semestres} : get all the semestres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semestres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SemestreDTO>> getAllSemestres(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Semestres");
        Page<SemestreDTO> page = semestreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /semestres/:id} : get the "id" semestre.
     *
     * @param id the id of the semestreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semestreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SemestreDTO> getSemestre(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Semestre : {}", id);
        Optional<SemestreDTO> semestreDTO = semestreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semestreDTO);
    }

    /**
     * {@code DELETE  /semestres/:id} : delete the "id" semestre.
     *
     * @param id the id of the semestreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemestre(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Semestre : {}", id);
        semestreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
