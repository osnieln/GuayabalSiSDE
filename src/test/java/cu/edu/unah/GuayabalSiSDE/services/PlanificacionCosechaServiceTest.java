package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.repository.PlanificacionCosechaRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.PlanificacionCosechaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanificacionCosechaServiceTest {

    @Mock
    private PlanificacionCosechaRepository planificacionCosechaRepository;

    @Mock
    private AreaCultivoService areaCultivoService;

    @InjectMocks
    private PlanificacionCosechaServiceImpl planificacionCosechaService;

    private AreaCultivoPk pk;
    private AreaCultivo areaCultivoPlanificada;

    @BeforeEach
    void setUp() {
        pk = AreaCultivoPk.builder()
                .areaId(1L)
                .cultivoId(2L)
                .fechaSiembra(Date.valueOf(LocalDate.now().minusMonths(1)))
                .build();

        areaCultivoPlanificada = AreaCultivo.builder()
                .areaCultivoPk(pk)
                .fechaRecogida(Date.valueOf(LocalDate.now().plusDays(10)))
                .planProd(1000L)
                .produccionReal(null)
                .area(Area.builder().id(1L).descripcion("Parcela Norte").build())
                .cultivo(Cultivo.builder().id(2L).descripcion("Maíz").build())
                .activo(true)
                .build();
    }

    @Test
    void findCalendario_CuandoNoExistePlanificacionPrevia_LaCreaComoPlanificada() {
        when(areaCultivoService.findByFechaRecogidaBetween(any(), any())).thenReturn(List.of(areaCultivoPlanificada));
        when(planificacionCosechaRepository.findById(pk)).thenReturn(Optional.empty());
        when(planificacionCosechaRepository.save(any(PlanificacionCosecha.class))).thenAnswer(inv -> inv.getArgument(0));

        List<PlanificacionCosechaResponse> resultado = planificacionCosechaService.findCalendario(
                Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusMonths(1)));

        assertEquals(1, resultado.size());
        assertEquals("PLANIFICADA", resultado.get(0).getEstado());
    }

    @Test
    void findCalendario_CuandoAreaCultivoYaTieneProduccionReal_LaCreaComoCompletada() {
        areaCultivoPlanificada.setProduccionReal(950.0);

        when(areaCultivoService.findByFechaRecogidaBetween(any(), any())).thenReturn(List.of(areaCultivoPlanificada));
        when(planificacionCosechaRepository.findById(pk)).thenReturn(Optional.empty());
        when(planificacionCosechaRepository.save(any(PlanificacionCosecha.class))).thenAnswer(inv -> inv.getArgument(0));

        List<PlanificacionCosechaResponse> resultado = planificacionCosechaService.findCalendario(
                Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusMonths(1)));

        assertEquals("COMPLETADA", resultado.get(0).getEstado());
        assertEquals(95.0, resultado.get(0).getCumplimientoPct());
    }

    @Test
    void completar_ActualizaProduccionRealYMarcaComoCompletada() {
        PlanificacionCosecha existente = PlanificacionCosecha.builder()
                .areaCultivoPk(pk)
                .estado(EstadoCosecha.PLANIFICADA)
                .fechaEstimada(areaCultivoPlanificada.getFechaRecogida())
                .build();

        when(areaCultivoService.findById(pk)).thenReturn(areaCultivoPlanificada);
        when(areaCultivoService.edit(any(AreaCultivo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(planificacionCosechaRepository.findById(pk)).thenReturn(Optional.of(existente));
        when(planificacionCosechaRepository.save(any(PlanificacionCosecha.class))).thenAnswer(inv -> inv.getArgument(0));

        PlanificacionCosechaResponse resultado = planificacionCosechaService.completar(pk, 1040.0);

        assertEquals("COMPLETADA", resultado.getEstado());
        assertEquals(104.0, resultado.getCumplimientoPct());
        assertEquals(1040.0, resultado.getProduccionReal());

        ArgumentCaptor<PlanificacionCosecha> captor = ArgumentCaptor.forClass(PlanificacionCosecha.class);
        verify(planificacionCosechaRepository).save(captor.capture());
        assertNotNull(captor.getValue().getFechaCompletado());
    }

    @Test
    void completar_ConProduccionRealNegativa_LanzaExcepcion() {
        assertThrows(BusinessValidationException.class, () -> planificacionCosechaService.completar(pk, -5.0));
        verify(areaCultivoService, never()).findById(any());
    }

    @Test
    void completar_CuandoAreaCultivoNoExiste_LanzaExcepcion() {
        when(areaCultivoService.findById(pk)).thenReturn(null);

        assertThrows(BusinessValidationException.class, () -> planificacionCosechaService.completar(pk, 100.0));
    }

    @Test
    void cancelar_MarcaComoCancelada() {
        when(areaCultivoService.findById(pk)).thenReturn(areaCultivoPlanificada);
        when(planificacionCosechaRepository.findById(pk)).thenReturn(Optional.empty());
        when(planificacionCosechaRepository.save(any(PlanificacionCosecha.class))).thenAnswer(inv -> inv.getArgument(0));

        PlanificacionCosechaResponse resultado = planificacionCosechaService.cancelar(pk);

        assertEquals("CANCELADA", resultado.getEstado());
    }

    @Test
    void actualizarVencidas_MarcaPlanificadasConFechaPasadaComoVencidas() {
        PlanificacionCosecha planificadaVencida = PlanificacionCosecha.builder()
                .areaCultivoPk(pk)
                .estado(EstadoCosecha.PLANIFICADA)
                .fechaEstimada(Date.valueOf(LocalDate.now().minusDays(1)))
                .build();

        when(planificacionCosechaRepository.findByEstadoAndFechaEstimadaBefore(eq(EstadoCosecha.PLANIFICADA), any()))
                .thenReturn(List.of(planificadaVencida));

        planificacionCosechaService.actualizarVencidas();

        assertEquals(EstadoCosecha.VENCIDA, planificadaVencida.getEstado());
        verify(planificacionCosechaRepository).saveAll(List.of(planificadaVencida));
    }
}
