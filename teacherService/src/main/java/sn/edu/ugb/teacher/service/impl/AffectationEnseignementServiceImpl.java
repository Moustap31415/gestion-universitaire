package sn.edu.ugb.teacher.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.teacher.domain.AffectationEnseignement;
import sn.edu.ugb.teacher.repository.AffectationEnseignementRepository;
import sn.edu.ugb.teacher.service.AffectationEnseignementService;
import sn.edu.ugb.teacher.service.dto.AffectationEnseignementDTO;
import sn.edu.ugb.teacher.service.mapper.AffectationEnseignementMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.teacher.domain.AffectationEnseignement}.
 */
@Service
@Transactional
public class AffectationEnseignementServiceImpl implements AffectationEnseignementService {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationEnseignementServiceImpl.class);

    private final AffectationEnseignementRepository affectationEnseignementRepository;

    private final AffectationEnseignementMapper affectationEnseignementMapper;

    public AffectationEnseignementServiceImpl(
        AffectationEnseignementRepository affectationEnseignementRepository,
        AffectationEnseignementMapper affectationEnseignementMapper
    ) {
        this.affectationEnseignementRepository = affectationEnseignementRepository;
        this.affectationEnseignementMapper = affectationEnseignementMapper;
    }

    @Override
    public AffectationEnseignementDTO save(AffectationEnseignementDTO affectationEnseignementDTO) {
        LOG.debug("Request to save AffectationEnseignement : {}", affectationEnseignementDTO);
        AffectationEnseignement affectationEnseignement = affectationEnseignementMapper.toEntity(affectationEnseignementDTO);
        affectationEnseignement = affectationEnseignementRepository.save(affectationEnseignement);
        return affectationEnseignementMapper.toDto(affectationEnseignement);
    }

    @Override
    public AffectationEnseignementDTO update(AffectationEnseignementDTO affectationEnseignementDTO) {
        LOG.debug("Request to update AffectationEnseignement : {}", affectationEnseignementDTO);
        AffectationEnseignement affectationEnseignement = affectationEnseignementMapper.toEntity(affectationEnseignementDTO);
        affectationEnseignement = affectationEnseignementRepository.save(affectationEnseignement);
        return affectationEnseignementMapper.toDto(affectationEnseignement);
    }

    @Override
    public Optional<AffectationEnseignementDTO> partialUpdate(AffectationEnseignementDTO affectationEnseignementDTO) {
        LOG.debug("Request to partially update AffectationEnseignement : {}", affectationEnseignementDTO);

        return affectationEnseignementRepository
            .findById(affectationEnseignementDTO.getId())
            .map(existingAffectationEnseignement -> {
                affectationEnseignementMapper.partialUpdate(existingAffectationEnseignement, affectationEnseignementDTO);

                return existingAffectationEnseignement;
            })
            .map(affectationEnseignementRepository::save)
            .map(affectationEnseignementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AffectationEnseignementDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AffectationEnseignements");
        return affectationEnseignementRepository.findAll(pageable).map(affectationEnseignementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AffectationEnseignementDTO> findOne(Long id) {
        LOG.debug("Request to get AffectationEnseignement : {}", id);
        return affectationEnseignementRepository.findById(id).map(affectationEnseignementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AffectationEnseignement : {}", id);
        affectationEnseignementRepository.deleteById(id);
    }
}
