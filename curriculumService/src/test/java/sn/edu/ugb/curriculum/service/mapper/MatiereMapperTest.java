package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.MatiereAsserts.*;
import static sn.edu.ugb.curriculum.domain.MatiereTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatiereMapperTest {

    private MatiereMapper matiereMapper;

    @BeforeEach
    void setUp() {
        matiereMapper = new MatiereMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMatiereSample1();
        var actual = matiereMapper.toEntity(matiereMapper.toDto(expected));
        assertMatiereAllPropertiesEquals(expected, actual);
    }
}
