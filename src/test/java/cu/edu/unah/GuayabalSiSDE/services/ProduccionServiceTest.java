package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.repository.ProduccionRepository;
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
class ProduccionServiceTest {

    @Mock
    private ProduccionRepository produccionRepository;

    @InjectMocks
    private ProduccionServiceImple produccionService;

    private Produccion produccion1;
    private Produccion produccion2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        produccion1 = Produccion.builder()
                .id(1L)
                .descripcion("Produccion 1")
                .build();

        produccion2 = Produccion.builder()
                .id(2L)
                .descripcion("Produccion 2")
                .build();
    }

    @Test
    void findAll_ShouldReturnAllProducciones() {
        // Arrange
        when(produccionRepository.findAll()).thenReturn(Arrays.asList(produccion1, produccion2));

        // Act
        List<Produccion> result = produccionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(produccionRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenProduccionExists_ShouldReturnProduccion() {
        // Arrange
        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));

        // Act
        Produccion result = produccionService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Produccion 1", result.getDescripcion());
        verify(produccionRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenProduccionNotExists_ShouldReturnNull() {
        // Arrange
        when(produccionRepository.findById(99L)).thenReturn(Optional.empty());
        // Act
        Produccion result = produccionService.findById(99L);
        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).findById(99L);
    }

    @Test
    void create_ShouldSaveAndReturnProduccion() {
        // Arrange
        Produccion newProduccion = Produccion.builder()
                .descripcion("New Produccion")
                .build();
        when(produccionRepository.save(any(Produccion.class))).thenReturn(newProduccion);

        // Act
        Produccion result = produccionService.create(newProduccion);

        // Assert
        assertNotNull(result);
        assertEquals("New Produccion", result.getDescripcion());
        verify(produccionRepository, times(1)).save(any(Produccion.class));
    }

    @Test
    void edit_WhenProduccionExists_ShouldUpdateAndReturnProduccion() {
        // Arrange
        Produccion updatedProduccion = Produccion.builder()
                .id(1L)
                .descripcion("Updated Produccion")
                .build();

        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));
        when(produccionRepository.save(any(Produccion.class))).thenReturn(updatedProduccion);

        // Act
        Produccion result = produccionService.edit(updatedProduccion);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Produccion", result.getDescripcion());
        verify(produccionRepository, times(1)).findById(1L);
        verify(produccionRepository, times(1)).save(any(Produccion.class));
    }

    @Test
    void edit_WhenProduccionNotExists_ShouldReturnNull() {
        // Arrange
        when(produccionRepository.findById(99L)).thenReturn(Optional.empty());
        Produccion nonExistentProduccion = Produccion.builder()
                .id(99L)
                .descripcion("Non-existent Produccion")
                .build();

        // Act
        Produccion result = produccionService.edit(nonExistentProduccion);

        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).findById(99L);
        verify(produccionRepository, never()).save(any(Produccion.class));
    }

    @Test
    void delete_WhenProduccionExists_ShouldDeleteAndReturnProduccion() {
        // Arrange
        when(produccionRepository.findById(1L)).thenReturn(Optional.of(produccion1));
        doNothing().when(produccionRepository).delete(produccion1);

        // Act
        Produccion result = produccionService.delete(1L);


        // Assert
        assertNotNull(result);
        assertEquals("Produccion 1", result.getDescripcion());
        verify(produccionRepository, times(1)).findById(1L);
        verify(produccionRepository, times(1)).delete(produccion1);
    }

    @Test
    void delete_WhenProduccionNotExists_ShouldReturnNull() {
        // Arrange
        when(produccionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Produccion result = produccionService.delete(99L);

        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).findById(99L);
        verify(produccionRepository, never()).delete(any(Produccion.class));
    }
}
