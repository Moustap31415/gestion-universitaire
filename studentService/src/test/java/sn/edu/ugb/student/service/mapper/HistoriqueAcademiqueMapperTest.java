package sn.edu.ugb.student.service.mapper;

import static sn.edu.ugb.student.domain.HistoriqueAcademiqueAsserts.*;
import static sn.edu.ugb.student.domain.HistoriqueAcademiqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueAcademiqueMapperTest {

    private HistoriqueAcademiqueMapper historiqueAcademiqueMapper;

    @BeforeEach
    void setUp() {
        historiqueAcademiqueMapper = new HistoriqueAcademiqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueAcademiqueSample1();
        var actual = historiqueAcademiqueMapper.toEntity(historiqueAcademiqueMapper.toDto(expected));
        assertHistoriqueAcademiqueAllPropertiesEquals(expected, actual);
    }
}
