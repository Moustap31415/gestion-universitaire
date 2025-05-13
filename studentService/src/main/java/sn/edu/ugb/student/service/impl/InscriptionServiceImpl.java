package sn.edu.ugb.student.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.student.domain.Inscription;
import sn.edu.ugb.student.repository.InscriptionRepository;
import sn.edu.ugb.student.service.InscriptionService;
import sn.edu.ugb.student.service.dto.InscriptionDTO;
import sn.edu.ugb.student.service.mapper.InscriptionMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.student.domain.Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;

    private final InscriptionMapper inscriptionMapper;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository, InscriptionMapper inscriptionMapper) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionMapper = inscriptionMapper;
    }

    @Override
    public InscriptionDTO save(InscriptionDTO inscriptionDTO) {
        LOG.debug("Request to save Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.toEntity(inscriptionDTO);
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDto(inscription);
    }

    @Override
    public InscriptionDTO update(InscriptionDTO inscriptionDTO) {
        LOG.debug("Request to update Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.toEntity(inscriptionDTO);
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDto(inscription);
    }

    @Override
    public Optional<InscriptionDTO> partialUpdate(InscriptionDTO inscriptionDTO) {
        LOG.debug("Request to partially update Inscription : {}", inscriptionDTO);

        return inscriptionRepository
            .findById(inscriptionDTO.getId())
            .map(existingInscription -> {
                inscriptionMapper.partialUpdate(existingInscription, inscriptionDTO);

                return existingInscription;
            })
            .map(inscriptionRepository::save)
            .map(inscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll(pageable).map(inscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findById(id).map(inscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
    }
}
