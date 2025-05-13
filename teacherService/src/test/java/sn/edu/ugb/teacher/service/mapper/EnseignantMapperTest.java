package sn.edu.ugb.teacher.service.mapper;

import static sn.edu.ugb.teacher.domain.EnseignantAsserts.*;
import static sn.edu.ugb.teacher.domain.EnseignantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnseignantMapperTest {

    private EnseignantMapper enseignantMapper;

    @BeforeEach
    void setUp() {
        enseignantMapper = new EnseignantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnseignantSample1();
        var actual = enseignantMapper.toEntity(enseignantMapper.toDto(expected));
        assertEnseignantAllPropertiesEquals(expected, actual);
    }
}
