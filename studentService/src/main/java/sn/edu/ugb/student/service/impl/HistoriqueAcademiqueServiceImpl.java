package sn.edu.ugb.student.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.student.domain.HistoriqueAcademique;
import sn.edu.ugb.student.repository.HistoriqueAcademiqueRepository;
import sn.edu.ugb.student.service.HistoriqueAcademiqueService;
import sn.edu.ugb.student.service.dto.HistoriqueAcademiqueDTO;
import sn.edu.ugb.student.service.mapper.HistoriqueAcademiqueMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.student.domain.HistoriqueAcademique}.
 */
@Service
@Transactional
public class HistoriqueAcademiqueServiceImpl implements HistoriqueAcademiqueService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueAcademiqueServiceImpl.class);

    private final HistoriqueAcademiqueRepository historiqueAcademiqueRepository;

    private final HistoriqueAcademiqueMapper historiqueAcademiqueMapper;

    public HistoriqueAcademiqueServiceImpl(
        HistoriqueAcademiqueRepository historiqueAcademiqueRepository,
        HistoriqueAcademiqueMapper historiqueAcademiqueMapper
    ) {
        this.historiqueAcademiqueRepository = historiqueAcademiqueRepository;
        this.historiqueAcademiqueMapper = historiqueAcademiqueMapper;
    }

    @Override
    public HistoriqueAcademiqueDTO save(HistoriqueAcademiqueDTO historiqueAcademiqueDTO) {
        LOG.debug("Request to save HistoriqueAcademique : {}", historiqueAcademiqueDTO);
        HistoriqueAcademique historiqueAcademique = historiqueAcademiqueMapper.toEntity(historiqueAcademiqueDTO);
        historiqueAcademique = historiqueAcademiqueRepository.save(historiqueAcademique);
        return historiqueAcademiqueMapper.toDto(historiqueAcademique);
    }

    @Override
    public HistoriqueAcademiqueDTO update(HistoriqueAcademiqueDTO historiqueAcademiqueDTO) {
        LOG.debug("Request to update HistoriqueAcademique : {}", historiqueAcademiqueDTO);
        HistoriqueAcademique historiqueAcademique = historiqueAcademiqueMapper.toEntity(historiqueAcademiqueDTO);
        historiqueAcademique = historiqueAcademiqueRepository.save(historiqueAcademique);
        return historiqueAcademiqueMapper.toDto(historiqueAcademique);
    }

    @Override
    public Optional<HistoriqueAcademiqueDTO> partialUpdate(HistoriqueAcademiqueDTO historiqueAcademiqueDTO) {
        LOG.debug("Request to partially update HistoriqueAcademique : {}", historiqueAcademiqueDTO);

        return historiqueAcademiqueRepository
            .findById(historiqueAcademiqueDTO.getId())
            .map(existingHistoriqueAcademique -> {
                historiqueAcademiqueMapper.partialUpdate(existingHistoriqueAcademique, historiqueAcademiqueDTO);

                return existingHistoriqueAcademique;
            })
            .map(historiqueAcademiqueRepository::save)
            .map(historiqueAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueAcademiqueDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HistoriqueAcademiques");
        return historiqueAcademiqueRepository.findAll(pageable).map(historiqueAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueAcademiqueDTO> findOne(Long id) {
        LOG.debug("Request to get HistoriqueAcademique : {}", id);
        return historiqueAcademiqueRepository.findById(id).map(historiqueAcademiqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HistoriqueAcademique : {}", id);
        historiqueAcademiqueRepository.deleteById(id);
    }
}
