package sn.edu.ugb.teacher.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.teacher.domain.Enseignant;
import sn.edu.ugb.teacher.repository.EnseignantRepository;
import sn.edu.ugb.teacher.service.EnseignantService;
import sn.edu.ugb.teacher.service.dto.EnseignantDTO;
import sn.edu.ugb.teacher.service.mapper.EnseignantMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.teacher.domain.Enseignant}.
 */
@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private static final Logger LOG = LoggerFactory.getLogger(EnseignantServiceImpl.class);

    private final EnseignantRepository enseignantRepository;

    private final EnseignantMapper enseignantMapper;

    public EnseignantServiceImpl(EnseignantRepository enseignantRepository, EnseignantMapper enseignantMapper) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantMapper = enseignantMapper;
    }

    @Override
    public EnseignantDTO save(EnseignantDTO enseignantDTO) {
        LOG.debug("Request to save Enseignant : {}", enseignantDTO);
        Enseignant enseignant = enseignantMapper.toEntity(enseignantDTO);
        enseignant = enseignantRepository.save(enseignant);
        return enseignantMapper.toDto(enseignant);
    }

    @Override
    public EnseignantDTO update(EnseignantDTO enseignantDTO) {
        LOG.debug("Request to update Enseignant : {}", enseignantDTO);
        Enseignant enseignant = enseignantMapper.toEntity(enseignantDTO);
        enseignant = enseignantRepository.save(enseignant);
        return enseignantMapper.toDto(enseignant);
    }

    @Override
    public Optional<EnseignantDTO> partialUpdate(EnseignantDTO enseignantDTO) {
        LOG.debug("Request to partially update Enseignant : {}", enseignantDTO);

        return enseignantRepository
            .findById(enseignantDTO.getId())
            .map(existingEnseignant -> {
                enseignantMapper.partialUpdate(existingEnseignant, enseignantDTO);

                return existingEnseignant;
            })
            .map(enseignantRepository::save)
            .map(enseignantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnseignantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Enseignants");
        return enseignantRepository.findAll(pageable).map(enseignantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnseignantDTO> findOne(Long id) {
        LOG.debug("Request to get Enseignant : {}", id);
        return enseignantRepository.findById(id).map(enseignantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Enseignant : {}", id);
        enseignantRepository.deleteById(id);
    }
}
