package sn.edu.ugb.teacher.service.mapper;

import static sn.edu.ugb.teacher.domain.AffectationEnseignementAsserts.*;
import static sn.edu.ugb.teacher.domain.AffectationEnseignementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffectationEnseignementMapperTest {

    private AffectationEnseignementMapper affectationEnseignementMapper;

    @BeforeEach
    void setUp() {
        affectationEnseignementMapper = new AffectationEnseignementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAffectationEnseignementSample1();
        var actual = affectationEnseignementMapper.toEntity(affectationEnseignementMapper.toDto(expected));
        assertAffectationEnseignementAllPropertiesEquals(expected, actual);
    }
}
