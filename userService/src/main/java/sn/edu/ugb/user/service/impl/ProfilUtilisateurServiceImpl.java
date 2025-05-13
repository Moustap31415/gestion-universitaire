package sn.edu.ugb.user.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.user.domain.ProfilUtilisateur;
import sn.edu.ugb.user.repository.ProfilUtilisateurRepository;
import sn.edu.ugb.user.service.ProfilUtilisateurService;
import sn.edu.ugb.user.service.dto.ProfilUtilisateurDTO;
import sn.edu.ugb.user.service.mapper.ProfilUtilisateurMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.user.domain.ProfilUtilisateur}.
 */
@Service
@Transactional
public class ProfilUtilisateurServiceImpl implements ProfilUtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilUtilisateurServiceImpl.class);

    private final ProfilUtilisateurRepository profilUtilisateurRepository;

    private final ProfilUtilisateurMapper profilUtilisateurMapper;

    public ProfilUtilisateurServiceImpl(
        ProfilUtilisateurRepository profilUtilisateurRepository,
        ProfilUtilisateurMapper profilUtilisateurMapper
    ) {
        this.profilUtilisateurRepository = profilUtilisateurRepository;
        this.profilUtilisateurMapper = profilUtilisateurMapper;
    }

    @Override
    public ProfilUtilisateurDTO save(ProfilUtilisateurDTO profilUtilisateurDTO) {
        LOG.debug("Request to save ProfilUtilisateur : {}", profilUtilisateurDTO);
        ProfilUtilisateur profilUtilisateur = profilUtilisateurMapper.toEntity(profilUtilisateurDTO);
        profilUtilisateur = profilUtilisateurRepository.save(profilUtilisateur);
        return profilUtilisateurMapper.toDto(profilUtilisateur);
    }

    @Override
    public ProfilUtilisateurDTO update(ProfilUtilisateurDTO profilUtilisateurDTO) {
        LOG.debug("Request to update ProfilUtilisateur : {}", profilUtilisateurDTO);
        ProfilUtilisateur profilUtilisateur = profilUtilisateurMapper.toEntity(profilUtilisateurDTO);
        profilUtilisateur = profilUtilisateurRepository.save(profilUtilisateur);
        return profilUtilisateurMapper.toDto(profilUtilisateur);
    }

    @Override
    public Optional<ProfilUtilisateurDTO> partialUpdate(ProfilUtilisateurDTO profilUtilisateurDTO) {
        LOG.debug("Request to partially update ProfilUtilisateur : {}", profilUtilisateurDTO);

        return profilUtilisateurRepository
            .findById(profilUtilisateurDTO.getId())
            .map(existingProfilUtilisateur -> {
                profilUtilisateurMapper.partialUpdate(existingProfilUtilisateur, profilUtilisateurDTO);

                return existingProfilUtilisateur;
            })
            .map(profilUtilisateurRepository::save)
            .map(profilUtilisateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfilUtilisateurDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfilUtilisateurs");
        return profilUtilisateurRepository.findAll(pageable).map(profilUtilisateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfilUtilisateurDTO> findOne(Long id) {
        LOG.debug("Request to get ProfilUtilisateur : {}", id);
        return profilUtilisateurRepository.findById(id).map(profilUtilisateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfilUtilisateur : {}", id);
        profilUtilisateurRepository.deleteById(id);
    }
}
