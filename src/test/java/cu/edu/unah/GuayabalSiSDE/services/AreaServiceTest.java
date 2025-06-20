package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.repository.AreaRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Polygon;
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
class AreaServiceTest {

    //TODO REVISAR ESTA CLASE PARA ADICIONAR LAS CAPAS GIS

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private AreaServiceImpl areaService;

    private Area area1;
    private Area area2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        area1 = Area.builder()
                .id(1L)
                .descripcion("Area 1")
                .capa("Capa 1")
                .build();

        area2 = Area.builder()
                .id(2L)
                .descripcion("Area 2")
                .capa("Capa 2")
                .build();
    }

    @Test
    void findAll_ShouldReturnAllAreas() {
        // Arrange
        when(areaRepository.findAll()).thenReturn(Arrays.asList(area1, area2));

        // Act
        List<Area> result = areaService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(areaRepository, times(1)).findAll();
    }

    @Test
    void findByID_WhenAreaExists_ShouldReturnArea() {
        // Arrange
        when(areaRepository.findById(1L)).thenReturn(Optional.of(area1));

        // Act
        Area result = areaService.findByID(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Area 1", result.getDescripcion());
        verify(areaRepository, times(1)).findById(1L);
    }

    @Test
    void findByID_WhenAreaNotExists_ShouldReturnNull() {
        // Arrange
        when(areaRepository.findById(99L)).thenReturn(Optional.empty());
        // Act
        Area result = areaService.findByID(99L);
        // Assert
        assertNull(result);
        verify(areaRepository, times(1)).findById(99L);
    }

    @Test
    void createArea_ShouldSaveAndReturnArea() {
        // Arrange
        Area newArea = Area.builder()
                .descripcion("New Area")
                .capa("New Capa")
                .build();
        when(areaRepository.save(any(Area.class))).thenReturn(newArea);

        // Act
        Area result = areaService.createArea(newArea);

        // Assert
        assertNotNull(result);
        assertEquals("New Area", result.getDescripcion());
        verify(areaRepository, times(1)).save(any(Area.class));
    }

    @Test
    void editArea_WhenAreaExists_ShouldUpdateAndReturnArea() {
        // Arrange
        Area updatedArea = Area.builder()
                .id(1L)
                .descripcion("Updated Area")
                .capa("Updated Capa")
                .build();

        when(areaRepository.findById(1L)).thenReturn(Optional.of(area1));
        when(areaRepository.save(any(Area.class))).thenReturn(updatedArea);

        // Act
        Area result = areaService.editArea(updatedArea);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Area", result.getDescripcion());
        verify(areaRepository, times(1)).findById(1L);
        verify(areaRepository, times(1)).save(any(Area.class));
    }

    @Test
    void editArea_WhenAreaNotExists_ShouldThrowException() {
        // Arrange
        Area nonExistentArea = Area.builder()
                .id(99L)
                .descripcion("Non-existent Area")
                .build();
        when(areaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> {
            areaService.editArea(nonExistentArea);
        });

        assertEquals(ErrorCodes.OPERATION_VALIDATION_ERROR, exception.getErrorCode());
        assertEquals("Esta área no existe.", exception.getMessage());
        verify(areaRepository, times(1)).findById(99L);
        verify(areaRepository, never()).save(any(Area.class));
    }

    @Test
    void deleteArea_WhenAreaExists_ShouldDeleteAndReturnArea() {
        // Arrange
        when(areaRepository.findById(1L)).thenReturn(Optional.of(area1));
        doNothing().when(areaRepository).delete(area1);

        // Act
        Area result = areaService.deleteArea(1L);


        // Assert
        assertNotNull(result);
        assertEquals("Area 1", result.getDescripcion());
        verify(areaRepository, times(1)).findById(1L);
        verify(areaRepository, times(1)).delete(area1);
    }

    @Test
    void deleteArea_WhenAreaNotExists_ShouldThrowException() {
        // Arrange
        when(areaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> {
            areaService.deleteArea(99L);
        });

        assertEquals(ErrorCodes.OPERATION_VALIDATION_ERROR, exception.getErrorCode());
        assertEquals("Esta área no existe.", exception.getMessage());
        verify(areaRepository, times(1)).findById(99L);
        verify(areaRepository, never()).delete(any(Area.class));
    }

    @Test
    void findDistinctCapa_ShouldReturnDistinctCapas() {
        // Arrange
        List<String> capas = Arrays.asList("Capa 1", "Capa 2");
        when(areaRepository.findDistinctCapa()).thenReturn(capas);

        // Act
        List<String> result = areaService.findDistinctCapa();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Capa 1"));
        assertTrue(result.contains("Capa 2"));
        verify(areaRepository, times(1)).findDistinctCapa();
    }
}
