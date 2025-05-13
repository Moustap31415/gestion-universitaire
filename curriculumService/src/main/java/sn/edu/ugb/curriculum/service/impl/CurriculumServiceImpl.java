package sn.edu.ugb.curriculum.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.domain.Curriculum;
import sn.edu.ugb.curriculum.repository.CurriculumRepository;
import sn.edu.ugb.curriculum.service.CurriculumService;
import sn.edu.ugb.curriculum.service.dto.CurriculumDTO;
import sn.edu.ugb.curriculum.service.mapper.CurriculumMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.curriculum.domain.Curriculum}.
 */
@Service
@Transactional
public class CurriculumServiceImpl implements CurriculumService {

    private static final Logger LOG = LoggerFactory.getLogger(CurriculumServiceImpl.class);

    private final CurriculumRepository curriculumRepository;

    private final CurriculumMapper curriculumMapper;

    public CurriculumServiceImpl(CurriculumRepository curriculumRepository, CurriculumMapper curriculumMapper) {
        this.curriculumRepository = curriculumRepository;
        this.curriculumMapper = curriculumMapper;
    }

    @Override
    public CurriculumDTO save(CurriculumDTO curriculumDTO) {
        LOG.debug("Request to save Curriculum : {}", curriculumDTO);
        Curriculum curriculum = curriculumMapper.toEntity(curriculumDTO);
        curriculum = curriculumRepository.save(curriculum);
        return curriculumMapper.toDto(curriculum);
    }

    @Override
    public CurriculumDTO update(CurriculumDTO curriculumDTO) {
        LOG.debug("Request to update Curriculum : {}", curriculumDTO);
        Curriculum curriculum = curriculumMapper.toEntity(curriculumDTO);
        curriculum = curriculumRepository.save(curriculum);
        return curriculumMapper.toDto(curriculum);
    }

    @Override
    public Optional<CurriculumDTO> partialUpdate(CurriculumDTO curriculumDTO) {
        LOG.debug("Request to partially update Curriculum : {}", curriculumDTO);

        return curriculumRepository
            .findById(curriculumDTO.getId())
            .map(existingCurriculum -> {
                curriculumMapper.partialUpdate(existingCurriculum, curriculumDTO);

                return existingCurriculum;
            })
            .map(curriculumRepository::save)
            .map(curriculumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurriculumDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Curricula");
        return curriculumRepository.findAll(pageable).map(curriculumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurriculumDTO> findOne(Long id) {
        LOG.debug("Request to get Curriculum : {}", id);
        return curriculumRepository.findById(id).map(curriculumMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Curriculum : {}", id);
        curriculumRepository.deleteById(id);
    }
}
