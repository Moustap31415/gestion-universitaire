package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.UniteEnseignementAsserts.*;
import static sn.edu.ugb.curriculum.domain.UniteEnseignementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniteEnseignementMapperTest {

    private UniteEnseignementMapper uniteEnseignementMapper;

    @BeforeEach
    void setUp() {
        uniteEnseignementMapper = new UniteEnseignementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUniteEnseignementSample1();
        var actual = uniteEnseignementMapper.toEntity(uniteEnseignementMapper.toDto(expected));
        assertUniteEnseignementAllPropertiesEquals(expected, actual);
    }
}
