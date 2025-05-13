package sn.edu.ugb.curriculum.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.domain.Filiere;
import sn.edu.ugb.curriculum.repository.FiliereRepository;
import sn.edu.ugb.curriculum.service.FiliereService;
import sn.edu.ugb.curriculum.service.dto.FiliereDTO;
import sn.edu.ugb.curriculum.service.mapper.FiliereMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.curriculum.domain.Filiere}.
 */
@Service
@Transactional
public class FiliereServiceImpl implements FiliereService {

    private static final Logger LOG = LoggerFactory.getLogger(FiliereServiceImpl.class);

    private final FiliereRepository filiereRepository;

    private final FiliereMapper filiereMapper;

    public FiliereServiceImpl(FiliereRepository filiereRepository, FiliereMapper filiereMapper) {
        this.filiereRepository = filiereRepository;
        this.filiereMapper = filiereMapper;
    }

    @Override
    public FiliereDTO save(FiliereDTO filiereDTO) {
        LOG.debug("Request to save Filiere : {}", filiereDTO);
        Filiere filiere = filiereMapper.toEntity(filiereDTO);
        filiere = filiereRepository.save(filiere);
        return filiereMapper.toDto(filiere);
    }

    @Override
    public FiliereDTO update(FiliereDTO filiereDTO) {
        LOG.debug("Request to update Filiere : {}", filiereDTO);
        Filiere filiere = filiereMapper.toEntity(filiereDTO);
        filiere = filiereRepository.save(filiere);
        return filiereMapper.toDto(filiere);
    }

    @Override
    public Optional<FiliereDTO> partialUpdate(FiliereDTO filiereDTO) {
        LOG.debug("Request to partially update Filiere : {}", filiereDTO);

        return filiereRepository
            .findById(filiereDTO.getId())
            .map(existingFiliere -> {
                filiereMapper.partialUpdate(existingFiliere, filiereDTO);

                return existingFiliere;
            })
            .map(filiereRepository::save)
            .map(filiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiliereDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Filieres");
        return filiereRepository.findAll(pageable).map(filiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiliereDTO> findOne(Long id) {
        LOG.debug("Request to get Filiere : {}", id);
        return filiereRepository.findById(id).map(filiereMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Filiere : {}", id);
        filiereRepository.deleteById(id);
    }
}
