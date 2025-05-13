package sn.edu.ugb.grade.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.grade.domain.SessionExamen;
import sn.edu.ugb.grade.repository.SessionExamenRepository;
import sn.edu.ugb.grade.service.SessionExamenService;
import sn.edu.ugb.grade.service.dto.SessionExamenDTO;
import sn.edu.ugb.grade.service.mapper.SessionExamenMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.grade.domain.SessionExamen}.
 */
@Service
@Transactional
public class SessionExamenServiceImpl implements SessionExamenService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionExamenServiceImpl.class);

    private final SessionExamenRepository sessionExamenRepository;

    private final SessionExamenMapper sessionExamenMapper;

    public SessionExamenServiceImpl(SessionExamenRepository sessionExamenRepository, SessionExamenMapper sessionExamenMapper) {
        this.sessionExamenRepository = sessionExamenRepository;
        this.sessionExamenMapper = sessionExamenMapper;
    }

    @Override
    public SessionExamenDTO save(SessionExamenDTO sessionExamenDTO) {
        LOG.debug("Request to save SessionExamen : {}", sessionExamenDTO);
        SessionExamen sessionExamen = sessionExamenMapper.toEntity(sessionExamenDTO);
        sessionExamen = sessionExamenRepository.save(sessionExamen);
        return sessionExamenMapper.toDto(sessionExamen);
    }

    @Override
    public SessionExamenDTO update(SessionExamenDTO sessionExamenDTO) {
        LOG.debug("Request to update SessionExamen : {}", sessionExamenDTO);
        SessionExamen sessionExamen = sessionExamenMapper.toEntity(sessionExamenDTO);
        sessionExamen = sessionExamenRepository.save(sessionExamen);
        return sessionExamenMapper.toDto(sessionExamen);
    }

    @Override
    public Optional<SessionExamenDTO> partialUpdate(SessionExamenDTO sessionExamenDTO) {
        LOG.debug("Request to partially update SessionExamen : {}", sessionExamenDTO);

        return sessionExamenRepository
            .findById(sessionExamenDTO.getId())
            .map(existingSessionExamen -> {
                sessionExamenMapper.partialUpdate(existingSessionExamen, sessionExamenDTO);

                return existingSessionExamen;
            })
            .map(sessionExamenRepository::save)
            .map(sessionExamenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SessionExamenDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SessionExamen");
        return sessionExamenRepository.findAll(pageable).map(sessionExamenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionExamenDTO> findOne(Long id) {
        LOG.debug("Request to get SessionExamen : {}", id);
        return sessionExamenRepository.findById(id).map(sessionExamenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SessionExamen : {}", id);
        sessionExamenRepository.deleteById(id);
    }
}
