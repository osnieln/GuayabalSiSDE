package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.repository.AlertaSistemaRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertaSistemaServiceTest {

    @Mock
    private AlertaSistemaRepository alertaSistemaRepository;

    @Mock
    private AreaCultivoService areaCultivoService;

    @Mock
    private RiegoService riegoService;

    @Mock
    private AgroquimicoService agroquimicoService;

    @InjectMocks
    private AlertaSistemaServiceImpl alertaSistemaService;

    private Agroquimico agroquimicoBajoStock;

    @BeforeEach
    void setUp() {
        agroquimicoBajoStock = Agroquimico.builder()
                .id(1L)
                .nombre("Sulfato de Cobre")
                .stockActual(2.0)
                .stockMinimo(10.0)
                .areaCultivos(new ArrayList<>())
                .build();
    }

    @Test
    void generarAlertas_CuandoNoExisteAlertaPrevia_CreaUnaNueva() {
        when(areaCultivoService.findCultivosPorVencer(anyInt())).thenReturn(Collections.emptyList());
        when(riegoService.findAreasSinRiego(anyInt())).thenReturn(Collections.emptyList());
        when(agroquimicoService.findBajoStock()).thenReturn(List.of(agroquimicoBajoStock));
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.AGROQUIMICO_BAJO_STOCK))
                .thenReturn(Collections.emptyList());
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.CULTIVO_POR_VENCER))
                .thenReturn(Collections.emptyList());
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.RIEGO_PENDIENTE))
                .thenReturn(Collections.emptyList());

        alertaSistemaService.generarAlertas();

        ArgumentCaptor<AlertaSistema> captor = ArgumentCaptor.forClass(AlertaSistema.class);
        verify(alertaSistemaRepository, times(1)).save(captor.capture());
        assertEquals(TipoAlerta.AGROQUIMICO_BAJO_STOCK, captor.getValue().getTipo());
        assertEquals(1L, captor.getValue().getAgroquimicoId());
    }

    @Test
    void generarAlertas_CuandoYaExisteAlertaActiva_NoDuplica() {
        AlertaSistema existente = AlertaSistema.builder()
                .id(10L)
                .tipo(TipoAlerta.AGROQUIMICO_BAJO_STOCK)
                .agroquimicoId(1L)
                .resuelta(false)
                .build();

        when(areaCultivoService.findCultivosPorVencer(anyInt())).thenReturn(Collections.emptyList());
        when(riegoService.findAreasSinRiego(anyInt())).thenReturn(Collections.emptyList());
        when(agroquimicoService.findBajoStock()).thenReturn(List.of(agroquimicoBajoStock));
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.AGROQUIMICO_BAJO_STOCK))
                .thenReturn(List.of(existente));
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.CULTIVO_POR_VENCER))
                .thenReturn(Collections.emptyList());
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.RIEGO_PENDIENTE))
                .thenReturn(Collections.emptyList());

        alertaSistemaService.generarAlertas();

        verify(alertaSistemaRepository, never()).save(any(AlertaSistema.class));
    }

    @Test
    void generarAlertas_CuandoElAgroquimicoYaNoEstaBajoStock_ResuelveLaAlertaExistente() {
        AlertaSistema existente = AlertaSistema.builder()
                .id(10L)
                .tipo(TipoAlerta.AGROQUIMICO_BAJO_STOCK)
                .agroquimicoId(1L)
                .resuelta(false)
                .build();

        when(areaCultivoService.findCultivosPorVencer(anyInt())).thenReturn(Collections.emptyList());
        when(riegoService.findAreasSinRiego(anyInt())).thenReturn(Collections.emptyList());
        when(agroquimicoService.findBajoStock()).thenReturn(Collections.emptyList());
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.AGROQUIMICO_BAJO_STOCK))
                .thenReturn(List.of(existente));
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.CULTIVO_POR_VENCER))
                .thenReturn(Collections.emptyList());
        when(alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.RIEGO_PENDIENTE))
                .thenReturn(Collections.emptyList());

        alertaSistemaService.generarAlertas();

        assertTrue(existente.isResuelta());
        assertNotNull(existente.getFechaResolucion());
        verify(alertaSistemaRepository, times(1)).save(existente);
    }

    @Test
    void marcarLeida_CuandoExiste_ActualizaLeidaYFecha() {
        AlertaSistema alerta = AlertaSistema.builder().id(5L).leida(false).build();
        when(alertaSistemaRepository.findById(5L)).thenReturn(Optional.of(alerta));
        when(alertaSistemaRepository.save(any(AlertaSistema.class))).thenAnswer(inv -> inv.getArgument(0));

        AlertaSistema resultado = alertaSistemaService.marcarLeida(5L);

        assertTrue(resultado.isLeida());
        assertNotNull(resultado.getFechaLectura());
    }

    @Test
    void marcarLeida_CuandoNoExiste_LanzaExcepcion() {
        when(alertaSistemaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessValidationException.class, () -> alertaSistemaService.marcarLeida(99L));
    }

    @Test
    void marcarResuelta_CuandoExiste_ActualizaResueltaYFecha() {
        AlertaSistema alerta = AlertaSistema.builder().id(7L).resuelta(false).build();
        when(alertaSistemaRepository.findById(7L)).thenReturn(Optional.of(alerta));
        when(alertaSistemaRepository.save(any(AlertaSistema.class))).thenAnswer(inv -> inv.getArgument(0));

        AlertaSistema resultado = alertaSistemaService.marcarResuelta(7L);

        assertTrue(resultado.isResuelta());
        assertNotNull(resultado.getFechaResolucion());
    }

    @Test
    void findAll_ConSoloNoLeidas_UsaElRepositorioCorrecto() {
        when(alertaSistemaRepository.findByLeidaFalseAndResueltaFalseOrderByFechaGeneracionDesc())
                .thenReturn(Collections.emptyList());

        alertaSistemaService.findAll(true);

        verify(alertaSistemaRepository, times(1)).findByLeidaFalseAndResueltaFalseOrderByFechaGeneracionDesc();
        verify(alertaSistemaRepository, never()).findByResueltaFalseOrderByFechaGeneracionDesc();
    }
}
