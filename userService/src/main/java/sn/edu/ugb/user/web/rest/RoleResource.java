package sn.edu.ugb.user.web.rest;

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
import sn.edu.ugb.user.repository.RoleRepository;
import sn.edu.ugb.user.service.RoleService;
import sn.edu.ugb.user.service.dto.RoleDTO;
import sn.edu.ugb.user.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing {@link sn.edu.ugb.user.domain.Role}.
 */
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role", description = "Gestion des rôles utilisateurs")
public class RoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "userServiceRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    public RoleResource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    /**
     * {@code POST  /roles} : Create a new role.
     *
     * @param roleDTO the roleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleDTO, or with status {@code 400 (Bad Request)} if the role has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(
        summary = "Créer un nouveau rôle",
        description = "Crée un nouveau rôle dans le système",
        responses = {
            @ApiResponse(responseCode = "201", description = "Rôle créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Le rôle existe déjà ou données incorrectes"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<RoleDTO> createRole(
        @Parameter(description = "DTO du rôle à créer", required = true)
        @Valid @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save Role : {}", roleDTO);
        if (roleDTO.getId() != null) {
            throw new BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roleDTO = roleService.save(roleDTO);
        return ResponseEntity.created(new URI("/api/roles/" + roleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roleDTO.getId().toString()))
            .body(roleDTO);
    }

    /**
     * {@code PUT  /roles/:id} : Updates an existing role.
     *
     * @param id the id of the roleDTO to save.
     * @param roleDTO the roleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleDTO,
     * or with status {@code 400 (Bad Request)} if the roleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un rôle",
        description = "Met à jour complètement un rôle existant",
        responses = {
            @ApiResponse(responseCode = "200", description = "Rôle mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "ID invalide ou données incorrectes"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<RoleDTO> updateRole(
        @Parameter(description = "ID du rôle à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO du rôle avec les nouvelles valeurs", required = true)
        @Valid @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Role : {}, {}", id, roleDTO);
        if (roleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roleDTO = roleService.update(roleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleDTO.getId().toString()))
            .body(roleDTO);
    }

    /**
     * {@code PATCH  /roles/:id} : Partial updates given fields of an existing role, field will ignore if it is null
     *
     * @param id the id of the roleDTO to save.
     * @param roleDTO the roleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleDTO,
     * or with status {@code 400 (Bad Request)} if the roleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        summary = "Mise à jour partielle d'un rôle",
        description = "Met à jour partiellement un rôle (seuls les champs non nuls seront modifiés)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Rôle mis à jour partiellement avec succès"),
            @ApiResponse(responseCode = "400", description = "ID invalide ou données incorrectes"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
        }
    )
    public ResponseEntity<RoleDTO> partialUpdateRole(
        @Parameter(description = "ID du rôle à mettre à jour", required = true)
        @PathVariable(value = "id", required = false) final Long id,
        @Parameter(description = "DTO partiel du rôle avec les champs à mettre à jour", required = true)
        @NotNull @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Role partially : {}, {}", id, roleDTO);
        if (roleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleDTO> result = roleService.partialUpdate(roleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /roles} : get all the roles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
     */
    @GetMapping("")
    @Operation(
        summary = "Lister tous les rôles",
        description = "Retourne une liste paginée de tous les rôles disponibles",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des rôles récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
        }
    )
    public ResponseEntity<List<RoleDTO>> getAllRoles(
        @Parameter(description = "Paramètres de pagination (page, size, sort)")
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Roles");
        Page<RoleDTO> page = roleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roles/:id} : get the "id" role.
     *
     * @param id the id of the roleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtenir un rôle par ID",
        description = "Retourne les détails d'un rôle spécifique",
        responses = {
            @ApiResponse(responseCode = "200", description = "Rôle trouvé"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé")
        }
    )
    public ResponseEntity<RoleDTO> getRole(
        @Parameter(description = "ID du rôle à récupérer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get Role : {}", id);
        Optional<RoleDTO> roleDTO = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleDTO);
    }

    /**
     * {@code DELETE  /roles/:id} : delete the "id" role.
     *
     * @param id the id of the roleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprimer un rôle",
        description = "Supprime définitivement un rôle du système",
        responses = {
            @ApiResponse(responseCode = "204", description = "Rôle supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé")
        }
    )
    public ResponseEntity<Void> deleteRole(
        @Parameter(description = "ID du rôle à supprimer", required = true)
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to delete Role : {}", id);
        roleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
