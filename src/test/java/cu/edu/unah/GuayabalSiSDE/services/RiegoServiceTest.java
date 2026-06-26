package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import cu.edu.unah.GuayabalSiSDE.repository.RiegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiegoServiceTest {

    @Mock
    private RiegoRepository riegoRepository;

    @Mock
    private AreaCultivoService areaCultivoService;

    @InjectMocks
    private RiegoServiceImpl riegoService;

    private AreaCultivo areaCultivo1;

    @BeforeEach
    void setUp() {
        AreaCultivoPk pk = AreaCultivoPk.builder()
                .areaId(1L)
                .cultivoId(1L)
                .fechaSiembra(new Date(System.currentTimeMillis()))
                .build();

        areaCultivo1 = AreaCultivo.builder()
                .areaCultivoPk(pk)
                .build();
    }

    @Test
    void findAreasSinRiego_ShouldReturnAreasFromRepository() {
        // Arrange
        when(riegoRepository.findAreasSinRiegoDesde(any(Date.class)))
                .thenReturn(Collections.singletonList(areaCultivo1));

        // Act
        List<AreaCultivo> result = riegoService.findAreasSinRiego(15);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAreaCultivoPk().getAreaId());

        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        verify(riegoRepository, times(1)).findAreasSinRiegoDesde(dateCaptor.capture());
        Date expectedLimite = Date.valueOf(LocalDate.now().minusDays(15));
        assertEquals(expectedLimite, dateCaptor.getValue());
    }

    @Test
    void findRiegosProximos_ShouldUseDateRangeWithFechaRealNull() {
        // Arrange
        Riego riego = Riego.builder()
                .id(1L)
                .fechaPlanificacion(Date.valueOf(LocalDate.now().plusDays(2)))
                .areaCultivo(areaCultivo1)
                .build();
        when(riegoRepository.findByFechaPlanificacionBetweenAndFechaRealIsNull(any(Date.class), any(Date.class)))
                .thenReturn(Collections.singletonList(riego));

        // Act
        List<Riego> result = riegoService.findRiegosProximos(7);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(riegoRepository, times(1))
                .findByFechaPlanificacionBetweenAndFechaRealIsNull(any(Date.class), any(Date.class));
    }
}
