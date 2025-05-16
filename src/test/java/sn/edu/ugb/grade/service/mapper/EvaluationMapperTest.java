package sn.edu.ugb.grade.service.mapper;

import static sn.edu.ugb.grade.domain.EvaluationAsserts.*;
import static sn.edu.ugb.grade.domain.EvaluationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluationMapperTest {

    private EvaluationMapper evaluationMapper;

    @BeforeEach
    void setUp() {
        evaluationMapper = new EvaluationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEvaluationSample1();
        var actual = evaluationMapper.toEntity(evaluationMapper.toDto(expected));
        assertEvaluationAllPropertiesEquals(expected, actual);
    }
}
