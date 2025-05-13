package sn.edu.ugb.grade.service.mapper;

import static sn.edu.ugb.grade.domain.SessionExamenAsserts.*;
import static sn.edu.ugb.grade.domain.SessionExamenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionExamenMapperTest {

    private SessionExamenMapper sessionExamenMapper;

    @BeforeEach
    void setUp() {
        sessionExamenMapper = new SessionExamenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSessionExamenSample1();
        var actual = sessionExamenMapper.toEntity(sessionExamenMapper.toDto(expected));
        assertSessionExamenAllPropertiesEquals(expected, actual);
    }
}
