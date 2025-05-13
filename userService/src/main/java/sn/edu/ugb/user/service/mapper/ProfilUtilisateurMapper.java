package sn.edu.ugb.user.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.user.domain.ProfilUtilisateur;
import sn.edu.ugb.user.service.dto.ProfilUtilisateurDTO;

/**
 * Mapper for the entity {@link ProfilUtilisateur} and its DTO {@link ProfilUtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilUtilisateurMapper extends EntityMapper<ProfilUtilisateurDTO, ProfilUtilisateur> {}
