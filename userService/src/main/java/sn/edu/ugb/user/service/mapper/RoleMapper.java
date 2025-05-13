package sn.edu.ugb.user.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.user.domain.Role;
import sn.edu.ugb.user.service.dto.RoleDTO;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {}
