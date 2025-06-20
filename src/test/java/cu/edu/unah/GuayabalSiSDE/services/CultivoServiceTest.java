package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.CultivoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.ProduccionRepository;
import cu.edu.unah.GuayabalSiSDE.repository.TipoCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CultivoServiceTest {

    @Mock
    private CultivoRepository cultivoRepository;

    @Mock
    private ProduccionRepository produccionRepository;

    @Mock
    private TipoCultivoRepository tipoCultivoRepository;

    @InjectMocks
    private CultivoServiceImpl cultivoService;

    private Cultivo cultivo1;
    private Cultivo cultivo2;
    private Produccion produccion1;
    private TipoCultivo tipoCultivo1;

    @BeforeEach
    void setUp() {
        // Initialize test data
        produccion1 = Produccion.builder()
                .id(1L)
                .descripcion("Produccion 1")
                .build();

        tipoCultivo1 = TipoCultivo.builder()
                .id(1L)
                .nombre("Tipo 1")
                .build();

        cultivo1 = Cultivo.builder()
                .id(1L)
                .descripcion("Cultivo 1")
                .produccion(produccion1)
                .tipoCultivo(tipoCultivo1)
                .build();

        cultivo2 = Cultivo.builder()
                .id(2L)
                .descripcion("Cultivo 2")
                .produccion(produccion1)
                .tipoCultivo(tipoCultivo1)
                .build();
    }

    @Test
    void findAll_ShouldReturnAllCultivos() {
        // Arrange
        when(cultivoRepository.findAll()).thenReturn(Arrays.asList(cultivo1, cultivo2));

        // Act
        List<Cultivo> result = cultivoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cultivoRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenCultivoExists_ShouldReturnCultivo() {
        // Arrange
        when(cultivoRepository.findById(1L)).thenReturn(Optional.of(cultivo1));

        // Act
        Cultivo result = cultivoService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Cultivo 1", result.getDescripcion());
        verify(cultivoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(cultivoRepository.findById(99L)).thenReturn(Optional.empty());
        // Act
        Cultivo result = cultivoService.findById(99L);
        // Assert
        assertNull(result);
        verify(cultivoRepository, times(1)).findById(99L);
    }

    @Test
    void findByProduccion_WhenProduccionExists_ShouldReturnCultivos() {
        // Arrange
        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));
        when(cultivoRepository.findCultivoByProduccion(produccion1)).thenReturn(Arrays.asList(cultivo1, cultivo2));

        // Act
        List<Cultivo> result = cultivoService.findByProduccion(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(produccionRepository, times(1)).findById(1L);
        verify(cultivoRepository, times(1)).findCultivoByProduccion(produccion1);
    }

    @Test
    void findByProduccion_WhenProduccionNotExists_ShouldThrowException() {
        // Arrange
        when(produccionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> {
            cultivoService.findByProduccion(99L);
        });

        assertEquals(ErrorCodes.OPERATION_VALIDATION_ERROR, exception.getErrorCode());
        assertEquals("Este cultivo no existe.", exception.getMessage());
        verify(produccionRepository, times(1)).findById(99L);
        verify(cultivoRepository, never()).findCultivoByProduccion(any());
    }

    @Test
    void create_WithValidData_ShouldSaveAndReturnCultivo() {
        // Arrange
        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));
        when(tipoCultivoRepository.findById(1L)).thenReturn(Optional.of(tipoCultivo1));
        when(cultivoRepository.save(any(Cultivo.class))).thenReturn(cultivo1);

        // Act
        Cultivo result = cultivoService.create(cultivo1);

        // Assert
        assertNotNull(result);
        assertEquals("Cultivo 1", result.getDescripcion());
        verify(produccionRepository, times(1)).findById(1L);
        verify(tipoCultivoRepository, times(1)).findById(1L);
        verify(cultivoRepository, times(1)).save(any(Cultivo.class));
    }

    @Test
    void create_WhenTipoCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));
        when(tipoCultivoRepository.findById(99L)).thenReturn(Optional.empty());
        Cultivo newCultivo = Cultivo.builder()
                .descripcion("New Cultivo")
                .produccion(produccion1)
                .tipoCultivo(TipoCultivo.builder().id(99L).build())
                .build();

        // Act
        Cultivo result = cultivoService.create(newCultivo);

        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).findById(1L);
        verify(tipoCultivoRepository, times(1)).findById(99L);
//        verify(cultivoRepository, never()).save(any());
    }

    @Test
    void edit_WhenCultivoExists_ShouldUpdateAndReturnCultivo() {
        // Arrange
        Cultivo updatedCultivo = Cultivo.builder()
                .id(1L)
                .descripcion("Updated Cultivo")
                .produccion(produccion1)
                .tipoCultivo(tipoCultivo1)
                .build();

        when(cultivoRepository.findById(1L)).thenReturn(Optional.of(cultivo1));
        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));
        when(tipoCultivoRepository.findById(1L)).thenReturn(Optional.of(tipoCultivo1));
        when(cultivoRepository.save(any(Cultivo.class))).thenReturn(updatedCultivo);

        // Act
        Cultivo result = cultivoService.edit(updatedCultivo);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Cultivo", result.getDescripcion());
        verify(cultivoRepository, times(1)).findById(1L);
        verify(produccionRepository, times(1)).findById(1L);
        verify(tipoCultivoRepository, times(1)).findById(1L);
        verify(cultivoRepository, times(1)).save(any(Cultivo.class));
    }

    @Test
    void edit_WhenCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(cultivoRepository.findById(99L)).thenReturn(Optional.empty());
        Cultivo nonExistentCultivo = Cultivo.builder()
                .id(99L)
                .descripcion("Non-existent Cultivo")
                .build();

        // Act
        Cultivo result = cultivoService.edit(nonExistentCultivo);

        // Assert
        assertNull(result);
        verify(cultivoRepository, times(1)).findById(99L);
        verify(produccionRepository, never()).findById(any());
        verify(tipoCultivoRepository, never()).findById(any());
        verify(cultivoRepository, never()).save(any());
    }

    @Test
    void delete_WhenCultivoExists_ShouldDeleteAndReturnCultivo() {
        // Arrange
        when(cultivoRepository.findById(1L)).thenReturn(Optional.of(cultivo1));
        doNothing().when(cultivoRepository).delete(cultivo1);

        // Act
        Cultivo result = cultivoService.delete(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Cultivo 1", result.getDescripcion());
        verify(cultivoRepository, times(1)).findById(1L);
        verify(cultivoRepository, times(1)).delete(cultivo1);
    }

    @Test
    void delete_WhenCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(cultivoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Cultivo result = cultivoService.delete(99L);

        // Assert
        assertNull(result);
        verify(cultivoRepository, times(1)).findById(99L);
        verify(cultivoRepository, never()).delete(any());
    }
}
