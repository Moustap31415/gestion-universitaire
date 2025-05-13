package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.CurriculumAsserts.*;
import static sn.edu.ugb.curriculum.domain.CurriculumTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurriculumMapperTest {

    private CurriculumMapper curriculumMapper;

    @BeforeEach
    void setUp() {
        curriculumMapper = new CurriculumMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCurriculumSample1();
        var actual = curriculumMapper.toEntity(curriculumMapper.toDto(expected));
        assertCurriculumAllPropertiesEquals(expected, actual);
    }
}
