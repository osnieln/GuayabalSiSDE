package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.AgroquimicoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgroquimicoServiceTest {

    @Mock
    private AgroquimicoRepository agroquimicoRepository;

    @Mock
    private AreaCultivoRepository areaCultivoRepository;

    @InjectMocks
    private AgroquimicoServiceImpl agroquimicoService;

    private Agroquimico agroquimico1;
    private Agroquimico agroquimico2;
    private AreaCultivo areaCultivo1;
    private AreaCultivo areaCultivo2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        agroquimico1 = Agroquimico.builder()
                .id(1L)
                .nombre("Herbicida X")
                .areaCultivos(new ArrayList<>())
                .build();

        agroquimico2 = Agroquimico.builder()
                .id(2L)
                .nombre("Fungicida Y")
                .areaCultivos(new ArrayList<>())
                .build();

        areaCultivo1 = new AreaCultivo();
        areaCultivo1.setAgroquimicos(new ArrayList<>());

        areaCultivo2 = new AreaCultivo();
        areaCultivo2.setAgroquimicos(new ArrayList<>());
    }

    @Test
    void findAll_ShouldReturnAllAgroquimicos() {
        // Arrange
        when(agroquimicoRepository.findAll()).thenReturn(Arrays.asList(agroquimico1, agroquimico2));

        // Act
        List<Agroquimico> result = agroquimicoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(agroquimicoRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenAgroquimicoExists_ShouldReturnAgroquimico() {
        // Arrange
        when(agroquimicoRepository.findById(1L)).thenReturn(Optional.of(agroquimico1));

        // Act
        Agroquimico result = agroquimicoService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Herbicida X", result.getNombre());
        verify(agroquimicoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenAgroquimicoNotExists_ShouldReturnNull() {
        // Arrange
        when(agroquimicoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Agroquimico result = agroquimicoService.findById(99L);

        // Assert
        assertNull(result);
        verify(agroquimicoRepository, times(1)).findById(99L);
    }

    @Test
    void findByNombre_WhenAgroquimicoExists_ShouldReturnAgroquimico() {
        // Arrange
        when(agroquimicoRepository.findByNombre("Herbicida X")).thenReturn(agroquimico1);

        // Act
        Agroquimico result = agroquimicoService.findByNombre("Herbicida X");


        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(agroquimicoRepository, times(1)).findByNombre("Herbicida X");
    }

    @Test
    void create_WhenAgroquimicoNotExists_ShouldSaveAndReturnAgroquimico() {
        // Arrange
        Agroquimico newAgroquimico = Agroquimico.builder()
                .nombre("Nuevo Agroquimico")
                .areaCultivos(new ArrayList<>())
                .build();

        when(agroquimicoRepository.findByNombre("Nuevo Agroquimico")).thenReturn(null);
        when(agroquimicoRepository.save(any(Agroquimico.class))).thenAnswer(invocation -> {
            Agroquimico a = invocation.getArgument(0);
            a.setId(3L);
            return a;
        });

        // Act
        Agroquimico result = agroquimicoService.create(newAgroquimico);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Nuevo Agroquimico", result.getNombre());
        verify(agroquimicoRepository, times(1)).findByNombre("Nuevo Agroquimico");
        verify(agroquimicoRepository, times(1)).save(any(Agroquimico.class));
    }

    @Test
    void create_WhenAgroquimicoExists_ShouldReturnNull() {
        // Arrange
        when(agroquimicoRepository.findByNombre("Herbicida X")).thenReturn(agroquimico1);

        // Act
        Agroquimico result = agroquimicoService.create(agroquimico1);

        // Assert
        assertNull(result);
        verify(agroquimicoRepository, times(1)).findByNombre("Herbicida X");
        verify(agroquimicoRepository, never()).save(any(Agroquimico.class));
    }

    @Test
    void edit_WhenAgroquimicoExists_ShouldUpdateAndReturnAgroquimico() {
        // Arrange
        Agroquimico updatedAgroquimico = Agroquimico.builder()
                .id(1L)
                .nombre("Herbicida X Modificado")
                .areaCultivos(new ArrayList<>())
                .build();

        when(agroquimicoRepository.findByNombre("Herbicida X Modificado")).thenReturn(agroquimico1);
        when(agroquimicoRepository.save(any(Agroquimico.class))).thenReturn(updatedAgroquimico);

        // Act
        Agroquimico result = agroquimicoService.edit(updatedAgroquimico);

        // Assert
        assertNotNull(result);
        assertEquals("Herbicida X Modificado", result.getNombre());
        verify(agroquimicoRepository, times(1)).findByNombre("Herbicida X Modificado");
    }

    @Test
    void delete_WhenAgroquimicoExistsAndNoAreaCultivos_ShouldDeleteAndReturnAgroquimico() {
        // Arrange
        when(agroquimicoRepository.findById(1L)).thenReturn(Optional.of(agroquimico1));
        doNothing().when(agroquimicoRepository).delete(agroquimico1);

        // Act
        Agroquimico result = agroquimicoService.delete(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Herbicida X", result.getNombre());
        verify(agroquimicoRepository, times(1)).findById(1L);
        verify(agroquimicoRepository, times(1)).delete(agroquimico1);
    }

    @Test
    void delete_WhenAgroquimicoHasAreaCultivos_ShouldReturnNull() {
        // Arrange
        agroquimico1.getAreaCultivos().add(areaCultivo1);
        when(agroquimicoRepository.findById(1L)).thenReturn(Optional.of(agroquimico1));

        // Act
        Agroquimico result = agroquimicoService.delete(1L);

        // Assert
        assertNull(result);
        verify(agroquimicoRepository, times(1)).findById(1L);
        verify(agroquimicoRepository, never()).delete(any(Agroquimico.class));
    }

    @Test
    void delete_WhenAgroquimicoNotExists_ShouldReturnNull() {
        // Arrange
        when(agroquimicoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Agroquimico result = agroquimicoService.delete(99L);

        // Assert
        assertNull(result);
        verify(agroquimicoRepository, times(1)).findById(99L);
        verify(agroquimicoRepository, never()).delete(any(Agroquimico.class));
    }
}
