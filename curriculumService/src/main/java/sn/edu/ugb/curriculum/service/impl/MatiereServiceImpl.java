package sn.edu.ugb.curriculum.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.domain.Matiere;
import sn.edu.ugb.curriculum.repository.MatiereRepository;
import sn.edu.ugb.curriculum.service.MatiereService;
import sn.edu.ugb.curriculum.service.dto.MatiereDTO;
import sn.edu.ugb.curriculum.service.mapper.MatiereMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.curriculum.domain.Matiere}.
 */
@Service
@Transactional
public class MatiereServiceImpl implements MatiereService {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereServiceImpl.class);

    private final MatiereRepository matiereRepository;

    private final MatiereMapper matiereMapper;

    public MatiereServiceImpl(MatiereRepository matiereRepository, MatiereMapper matiereMapper) {
        this.matiereRepository = matiereRepository;
        this.matiereMapper = matiereMapper;
    }

    @Override
    public MatiereDTO save(MatiereDTO matiereDTO) {
        LOG.debug("Request to save Matiere : {}", matiereDTO);
        Matiere matiere = matiereMapper.toEntity(matiereDTO);
        matiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(matiere);
    }

    @Override
    public MatiereDTO update(MatiereDTO matiereDTO) {
        LOG.debug("Request to update Matiere : {}", matiereDTO);
        Matiere matiere = matiereMapper.toEntity(matiereDTO);
        matiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(matiere);
    }

    @Override
    public Optional<MatiereDTO> partialUpdate(MatiereDTO matiereDTO) {
        LOG.debug("Request to partially update Matiere : {}", matiereDTO);

        return matiereRepository
            .findById(matiereDTO.getId())
            .map(existingMatiere -> {
                matiereMapper.partialUpdate(existingMatiere, matiereDTO);

                return existingMatiere;
            })
            .map(matiereRepository::save)
            .map(matiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatiereDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Matieres");
        return matiereRepository.findAll(pageable).map(matiereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MatiereDTO> findOne(Long id) {
        LOG.debug("Request to get Matiere : {}", id);
        return matiereRepository.findById(id).map(matiereMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
    }
}
