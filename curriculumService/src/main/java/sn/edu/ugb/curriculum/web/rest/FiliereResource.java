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
    @PutMapping("/{id}")
    public ResponseEntity<FiliereDTO> updateFiliere(
        @PathVariable(value = "id", required = false) final Long id,
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
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiliereDTO> partialUpdateFiliere(
        @PathVariable(value = "id", required = false) final Long id,
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
    @GetMapping("/{id}")
    public ResponseEntity<FiliereDTO> getFiliere(@PathVariable("id") Long id) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Filiere : {}", id);
        filiereService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
