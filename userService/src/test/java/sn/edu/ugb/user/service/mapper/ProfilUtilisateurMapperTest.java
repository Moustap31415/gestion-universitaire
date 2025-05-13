package sn.edu.ugb.user.service.mapper;

import static sn.edu.ugb.user.domain.ProfilUtilisateurAsserts.*;
import static sn.edu.ugb.user.domain.ProfilUtilisateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfilUtilisateurMapperTest {

    private ProfilUtilisateurMapper profilUtilisateurMapper;

    @BeforeEach
    void setUp() {
        profilUtilisateurMapper = new ProfilUtilisateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfilUtilisateurSample1();
        var actual = profilUtilisateurMapper.toEntity(profilUtilisateurMapper.toDto(expected));
        assertProfilUtilisateurAllPropertiesEquals(expected, actual);
    }
}
