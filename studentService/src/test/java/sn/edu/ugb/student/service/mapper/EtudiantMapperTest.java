package sn.edu.ugb.student.service.mapper;

import static sn.edu.ugb.student.domain.EtudiantAsserts.*;
import static sn.edu.ugb.student.domain.EtudiantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EtudiantMapperTest {

    private EtudiantMapper etudiantMapper;

    @BeforeEach
    void setUp() {
        etudiantMapper = new EtudiantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEtudiantSample1();
        var actual = etudiantMapper.toEntity(etudiantMapper.toDto(expected));
        assertEtudiantAllPropertiesEquals(expected, actual);
    }
}
