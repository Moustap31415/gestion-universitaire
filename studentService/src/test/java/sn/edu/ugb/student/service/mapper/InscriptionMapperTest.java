package sn.edu.ugb.student.service.mapper;

import static sn.edu.ugb.student.domain.InscriptionAsserts.*;
import static sn.edu.ugb.student.domain.InscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InscriptionMapperTest {

    private InscriptionMapper inscriptionMapper;

    @BeforeEach
    void setUp() {
        inscriptionMapper = new InscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInscriptionSample1();
        var actual = inscriptionMapper.toEntity(inscriptionMapper.toDto(expected));
        assertInscriptionAllPropertiesEquals(expected, actual);
    }
}
