package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AreaCultivoServiceTest {

    @Mock
    private AreaCultivoRepository areaCultivoRepository;

    @Mock
    private AreaService areaService;

    @Mock
    private CultivoService cultivoService;

    @Mock
    private AgroquimicoService agroquimicoService;

    @InjectMocks
    private AreaCultivoServiceImpl areaCultivoService;

    private AreaCultivo areaCultivo1;
    private AreaCultivo areaCultivo2;
    private AreaCultivoPk areaCultivoPk1;
    private Area area1;
    private Cultivo cultivo1;
    private Agroquimico agroquimico1;

    @BeforeEach
    void setUp() {
        // Initialize test data
        area1 = Area.builder()
                .id(1L)
                .descripcion("Area 1")
                .build();

        cultivo1 = Cultivo.builder()
                .id(1L)
                .descripcion("Cultivo 1")
                .build();

        agroquimico1 = Agroquimico.builder()
                .id(1L)
                .nombre("Agroquimico 1")
                .build();

        areaCultivoPk1 = AreaCultivoPk.builder()
                .areaId(1L)
                .cultivoId(1L)
                .fechaSiembra(new Date(System.currentTimeMillis()))
                .build();

        areaCultivo1 = AreaCultivo.builder()
                .areaCultivoPk(areaCultivoPk1)
                .fechaRecogida(new Date(System.currentTimeMillis()))
                .planProd(100L)
                .prodCultivosPermanente(50.0)
                .prodCultivosTemporales(50.0)
                .produccionReal(95.0)
                .area(area1)
                .cultivo(cultivo1)
                .agroquimicos(new ArrayList<>(Collections.singletonList(agroquimico1)))
                .build();

        AreaCultivoPk areaCultivoPk2 = AreaCultivoPk.builder()
                .areaId(2L)
                .cultivoId(2L)
                .fechaSiembra(new Date(System.currentTimeMillis()))
                .build();

        areaCultivo2 = AreaCultivo.builder()
                .areaCultivoPk(areaCultivoPk2)
                .fechaRecogida(new Date(System.currentTimeMillis()))
                .planProd(200L)
                .prodCultivosPermanente(100.0)
                .prodCultivosTemporales(100.0)
                .produccionReal(190.0)
                .build();
    }

    @Test
    void findAll_ShouldReturnAllAreaCultivos() {
        // Arrange
        when(areaCultivoRepository.findAll()).thenReturn(Arrays.asList(areaCultivo1, areaCultivo2));

        // Act
        List<AreaCultivo> result = areaCultivoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(areaCultivoRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenAreaCultivoExists_ShouldReturnAreaCultivo() {
        // Arrange
        when(areaCultivoRepository.findById(areaCultivoPk1)).thenReturn(Optional.of(areaCultivo1));

        // Act
        AreaCultivo result = areaCultivoService.findById(areaCultivoPk1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAreaCultivoPk().getAreaId());
        assertEquals(1L, result.getAreaCultivoPk().getCultivoId());
        verify(areaCultivoRepository, times(1)).findById(areaCultivoPk1);
    }

    @Test
    void findById_WhenAreaCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(areaCultivoRepository.findById(any(AreaCultivoPk.class))).thenReturn(Optional.empty());

        // Act
        AreaCultivo result = areaCultivoService.findById(AreaCultivoPk
                .builder()
                .areaId(1L)
                .cultivoId(1L)
                .fechaSiembra(new Date(System.currentTimeMillis()))
                .build());

        // Assert
        assertNull(result);
        verify(areaCultivoRepository, times(1)).findById(any(AreaCultivoPk.class));
    }

    @Test
    void create_WhenValidAreaCultivo_ShouldSaveAndReturnAreaCultivo() {
        // Arrange
        when(areaService.findByID(1L)).thenReturn(area1);
        when(cultivoService.findById(1L)).thenReturn(cultivo1);
//        when(agroquimicoService.findById(1L)).thenReturn(agroquimico1);
        when(areaCultivoRepository.save(any(AreaCultivo.class))).thenReturn(areaCultivo1);

        // Act
        AreaCultivo result = areaCultivoService.create(areaCultivo1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAreaCultivoPk().getAreaId());
        verify(areaCultivoRepository, times(1)).save(any(AreaCultivo.class));
    }

    @Test
    void create_WhenAreaNotExists_ShouldThrowException() {
        // Arrange
        when(areaService.findByID(1L)).thenReturn(null);

        // Act & Assert
        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> {
            areaCultivoService.create(areaCultivo1);
        });

        assertEquals(ErrorCodes.OPERATION_VALIDATION_ERROR, exception.getErrorCode());
        assertEquals("Esta área no existe.", exception.getMessage());
        verify(areaCultivoRepository, never()).save(any(AreaCultivo.class));
    }

    @Test
    void edit_WhenAreaCultivoExists_ShouldUpdateAndReturnAreaCultivo() {
        // Arrange
        when(areaCultivoRepository.findById(areaCultivoPk1)).thenReturn(Optional.of(areaCultivo1));
        when(areaCultivoRepository.save(any(AreaCultivo.class))).thenReturn(areaCultivo1);

        // Act
        AreaCultivo result = areaCultivoService.edit(areaCultivo1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAreaCultivoPk().getAreaId());
        verify(areaCultivoRepository, times(1)).save(any(AreaCultivo.class));
    }

    @Test
    void edit_WhenAreaCultivoNotExists_ShouldThrowException() {
        // Arrange
        when(areaCultivoRepository.findById(areaCultivoPk1)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> {
            areaCultivoService.edit(areaCultivo1);
        });

        assertEquals(ErrorCodes.OPERATION_VALIDATION_ERROR, exception.getErrorCode());
        assertEquals("Esta área no existe.", exception.getMessage());
        verify(areaCultivoRepository, never()).save(any(AreaCultivo.class));
    }

    @Test
    void delete_WhenAreaCultivoExists_ShouldDeleteAndReturnAreaCultivo() {
        // Arrange
        when(areaCultivoRepository.findById(areaCultivoPk1)).thenReturn(Optional.of(areaCultivo1));
        doNothing().when(areaCultivoRepository).delete(areaCultivo1);

        // Act
        AreaCultivo result = areaCultivoService.delete(areaCultivoPk1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAreaCultivoPk().getAreaId());
        verify(areaCultivoRepository, times(1)).delete(areaCultivo1);
    }

    @Test
    void delete_WhenAreaCultivoNotExists_ShouldThrowException() {
        // Arrange
        when(areaCultivoRepository.findById(areaCultivoPk1)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> {
            areaCultivoService.delete(areaCultivoPk1);
        });

        assertEquals(ErrorCodes.OPERATION_VALIDATION_ERROR, exception.getErrorCode());
        assertEquals("Esta área no existe.", exception.getMessage());
        verify(areaCultivoRepository, never()).delete(any(AreaCultivo.class));
    }

    @Test
    void findAreaCultivoByPlanProdBetween_ShouldReturnMatchingAreaCultivos() {
        // Arrange
        when(areaCultivoRepository.findAreaCultivoByPlanProdBetween(100L, 200L))
                .thenReturn(Collections.singletonList(areaCultivo1));

        // Act
        List<AreaCultivo> result = areaCultivoService.findAreaCultivoByPlanProdBetween(100L, 200L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getPlanProd());
    }

    @Test
    void findAreaCultivoByProdCultivosPermanenteAfter_ShouldReturnMatchingAreaCultivos() {
        // Arrange
        when(areaCultivoRepository.findAreaCultivoByProdCultivosPermanenteAfter(40.0))
                .thenReturn(Collections.singletonList(areaCultivo1));

        // Act
        List<AreaCultivo> result = areaCultivoService.findAreaCultivoByProdCultivosPermanenteAfter(40.0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getProdCultivosPermanente() > 40.0);
    }

    @Test
    void findAreaCultivoByFechaRecogidaBefore_ShouldReturnMatchingAreaCultivos() {
        // Arrange
        Date testDate = new Date(System.currentTimeMillis());
        when(areaCultivoRepository.findAreaCultivoByFechaRecogidaBefore(testDate))
                .thenReturn(Collections.singletonList(areaCultivo1));

        // Act
        List<AreaCultivo> result = areaCultivoService.findAreaCultivoByFechaRecogidaBefore(testDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getFechaRecogida());
    }
}
