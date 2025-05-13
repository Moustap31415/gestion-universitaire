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
    @PutMapping("/{id}")
    public ResponseEntity<CurriculumDTO> updateCurriculum(
        @PathVariable(value = "id", required = false) final Long id,
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
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurriculumDTO> partialUpdateCurriculum(
        @PathVariable(value = "id", required = false) final Long id,
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
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumDTO> getCurriculum(@PathVariable("id") Long id) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Curriculum : {}", id);
        curriculumService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
