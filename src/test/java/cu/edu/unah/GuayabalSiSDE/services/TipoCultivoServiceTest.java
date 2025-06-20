package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.TipoCultivoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoCultivoServiceTest {

    @Mock
    private TipoCultivoRepository tipoCultivoRepository;

    @InjectMocks
    private TipoCultivoServiceImpl tipoCultivoService;

    private TipoCultivo tipoCultivo1;
    private TipoCultivo tipoCultivo2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        tipoCultivo1 = TipoCultivo.builder()
                .id(1L)
                .nombre("Papa")
                .build();

        tipoCultivo2 = TipoCultivo.builder()
                .id(2L)
                .nombre("Yuca")
                .build();
    }

    @Test
    void findAll() {
        // Arrange
        List<TipoCultivo> tiposCultivo = Arrays.asList(tipoCultivo1, tipoCultivo2);
        when(tipoCultivoRepository.findAll(Sort.by("id"))).thenReturn(tiposCultivo);

        // Act
        List<TipoCultivo> result = tipoCultivoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Papa", result.get(0).getNombre());
        verify(tipoCultivoRepository, times(1)).findAll(Sort.by("id"));
    }


    @Test
    void findById_WhenTipoCultivoExists_ShouldReturnTipoCultivo() {
        // Arrange
        when(tipoCultivoRepository.findById(1L)).thenReturn(Optional.of(tipoCultivo1));

        // Act
        TipoCultivo result = tipoCultivoService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Papa", result.getNombre());
        verify(tipoCultivoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenTipoCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(tipoCultivoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        TipoCultivo result = tipoCultivoService.findById(99L);


        // Assert
        assertNull(result);
        verify(tipoCultivoRepository, times(1)).findById(99L);
    }


    @Test
    void findByNombre_WhenTipoCultivoExists_ShouldReturnTipoCultivo() {
        // Arrange
        when(tipoCultivoRepository.findByNombre("Papa")).thenReturn(tipoCultivo1);


        // Act
        TipoCultivo result = tipoCultivoService.findByNombre("Papa");


        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tipoCultivoRepository, times(1)).findByNombre("Papa");
    }

    @Test
    void create_WhenTipoCultivoNotExists_ShouldSaveAndReturnTipoCultivo() {
        // Arrange
        TipoCultivo newTipoCultivo = TipoCultivo.builder()
                .nombre("Nuevo")
                .build();

        when(tipoCultivoRepository.findByNombre("Nuevo")).thenReturn(null);
        when(tipoCultivoRepository.save(any(TipoCultivo.class))).thenAnswer(invocation -> {
            TipoCultivo tc = invocation.getArgument(0);
            tc.setId(3L);
            return tc;
        });

        // Act
        TipoCultivo result = tipoCultivoService.create(newTipoCultivo);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Nuevo", result.getNombre());
        verify(tipoCultivoRepository, times(1)).findByNombre("Nuevo");
        verify(tipoCultivoRepository, times(1)).save(any(TipoCultivo.class));
    }


    @Test
    void create_WhenTipoCultivoExists_ShouldReturnNull() {
        // Arrange
        when(tipoCultivoRepository.findByNombre("Papa")).thenReturn(tipoCultivo1);

        // Act
        TipoCultivo result = tipoCultivoService.create(tipoCultivo1);


        // Assert
        assertNull(result);
        verify(tipoCultivoRepository, times(1)).findByNombre("Papa");
        verify(tipoCultivoRepository, never()).save(any(TipoCultivo.class));
    }

    @Test
    void edit_WhenTipoCultivoExists_ShouldUpdateAndReturnTipoCultivo() {
        // Arrange
        TipoCultivo updatedTipoCultivo = TipoCultivo.builder()
                .id(1L)
                .nombre("Papa Modificada")
                .build();

        when(tipoCultivoRepository.findById(1L)).thenReturn(Optional.of(tipoCultivo1));
        when(tipoCultivoRepository.save(any(TipoCultivo.class))).thenReturn(updatedTipoCultivo);

        // Act
        TipoCultivo result = tipoCultivoService.edit(updatedTipoCultivo);


        // Assert
        assertNotNull(result);
        assertEquals("Papa Modificada", result.getNombre());
        verify(tipoCultivoRepository, times(1)).findById(1L);
        verify(tipoCultivoRepository, times(1)).save(any(TipoCultivo.class));
    }


    @Test
    void edit_WhenTipoCultivoNotExists_ShouldReturnNull() {
        // Arrange
        TipoCultivo nonExistentTipoCultivo = TipoCultivo.builder()
                .id(99L)
                .nombre("No existe")
                .build();

        when(tipoCultivoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        TipoCultivo result = tipoCultivoService.edit(nonExistentTipoCultivo);


        // Assert
        assertNull(result);
        verify(tipoCultivoRepository, times(1)).findById(99L);
        verify(tipoCultivoRepository, never()).save(any(TipoCultivo.class));
    }

    @Test
    void delete_WhenTipoCultivoExists_ShouldDeleteAndReturnTipoCultivo() {
        // Arrange
        when(tipoCultivoRepository.findById(1L)).thenReturn(Optional.of(tipoCultivo1));
        doNothing().when(tipoCultivoRepository).delete(tipoCultivo1);

        // Act
        TipoCultivo result = tipoCultivoService.delete(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Papa", result.getNombre());
        verify(tipoCultivoRepository, times(1)).findById(1L);
        verify(tipoCultivoRepository, times(1)).delete(tipoCultivo1);
    }


    @Test
    void delete_WhenTipoCultivoNotExists_ShouldReturnNull() {
        // Arrange
        when(tipoCultivoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        TipoCultivo result = tipoCultivoService.delete(99L);

        // Assert
        assertNull(result);
        verify(tipoCultivoRepository, times(1)).findById(99L);
        verify(tipoCultivoRepository, never()).delete(any(TipoCultivo.class));
    }
}
