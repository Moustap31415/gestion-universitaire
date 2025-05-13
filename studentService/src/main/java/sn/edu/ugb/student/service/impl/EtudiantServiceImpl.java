package sn.edu.ugb.student.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.student.domain.Etudiant;
import sn.edu.ugb.student.repository.EtudiantRepository;
import sn.edu.ugb.student.service.EtudiantService;
import sn.edu.ugb.student.service.dto.EtudiantDTO;
import sn.edu.ugb.student.service.mapper.EtudiantMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.student.domain.Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private static final Logger LOG = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
    }

    @Override
    public EtudiantDTO save(EtudiantDTO etudiantDTO) {
        LOG.debug("Request to save Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    @Override
    public EtudiantDTO update(EtudiantDTO etudiantDTO) {
        LOG.debug("Request to update Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    @Override
    public Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO) {
        LOG.debug("Request to partially update Etudiant : {}", etudiantDTO);

        return etudiantRepository
            .findById(etudiantDTO.getId())
            .map(existingEtudiant -> {
                etudiantMapper.partialUpdate(existingEtudiant, etudiantDTO);

                return existingEtudiant;
            })
            .map(etudiantRepository::save)
            .map(etudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Etudiants");
        return etudiantRepository.findAll(pageable).map(etudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtudiantDTO> findOne(Long id) {
        LOG.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findById(id).map(etudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
    }
}
