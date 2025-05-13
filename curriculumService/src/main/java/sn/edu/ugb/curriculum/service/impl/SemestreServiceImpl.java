package sn.edu.ugb.curriculum.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.domain.Semestre;
import sn.edu.ugb.curriculum.repository.SemestreRepository;
import sn.edu.ugb.curriculum.service.SemestreService;
import sn.edu.ugb.curriculum.service.dto.SemestreDTO;
import sn.edu.ugb.curriculum.service.mapper.SemestreMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.curriculum.domain.Semestre}.
 */
@Service
@Transactional
public class SemestreServiceImpl implements SemestreService {

    private static final Logger LOG = LoggerFactory.getLogger(SemestreServiceImpl.class);

    private final SemestreRepository semestreRepository;

    private final SemestreMapper semestreMapper;

    public SemestreServiceImpl(SemestreRepository semestreRepository, SemestreMapper semestreMapper) {
        this.semestreRepository = semestreRepository;
        this.semestreMapper = semestreMapper;
    }

    @Override
    public SemestreDTO save(SemestreDTO semestreDTO) {
        LOG.debug("Request to save Semestre : {}", semestreDTO);
        Semestre semestre = semestreMapper.toEntity(semestreDTO);
        semestre = semestreRepository.save(semestre);
        return semestreMapper.toDto(semestre);
    }

    @Override
    public SemestreDTO update(SemestreDTO semestreDTO) {
        LOG.debug("Request to update Semestre : {}", semestreDTO);
        Semestre semestre = semestreMapper.toEntity(semestreDTO);
        semestre = semestreRepository.save(semestre);
        return semestreMapper.toDto(semestre);
    }

    @Override
    public Optional<SemestreDTO> partialUpdate(SemestreDTO semestreDTO) {
        LOG.debug("Request to partially update Semestre : {}", semestreDTO);

        return semestreRepository
            .findById(semestreDTO.getId())
            .map(existingSemestre -> {
                semestreMapper.partialUpdate(existingSemestre, semestreDTO);

                return existingSemestre;
            })
            .map(semestreRepository::save)
            .map(semestreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SemestreDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Semestres");
        return semestreRepository.findAll(pageable).map(semestreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemestreDTO> findOne(Long id) {
        LOG.debug("Request to get Semestre : {}", id);
        return semestreRepository.findById(id).map(semestreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Semestre : {}", id);
        semestreRepository.deleteById(id);
    }
}
