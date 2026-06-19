package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.MovimientoAgroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.TipoMovimiento;
import cu.edu.unah.GuayabalSiSDE.repository.AgroquimicoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.MovimientoAgroquimicoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoAgroquimicoServiceImpl implements MovimientoAgroquimicoService {

    @Autowired
    private final MovimientoAgroquimicoRepository movimientoAgroquimicoRepository;

    @Autowired
    private final AgroquimicoRepository agroquimicoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoAgroquimico> findAll() {
        return movimientoAgroquimicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoAgroquimico findById(Long id) {
        return movimientoAgroquimicoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoAgroquimico> findByAgroquimico(Long agroquimicoId) {
        return movimientoAgroquimicoRepository.findByAgroquimicoIdOrderByFechaDesc(agroquimicoId);
    }

    @Override
    @Transactional
    public MovimientoAgroquimico create(MovimientoAgroquimico movimientoAgroquimico) {
        if (movimientoAgroquimico.getAgroquimico() == null || movimientoAgroquimico.getAgroquimico().getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El agroquímico del movimiento es obligatorio.");
        if (movimientoAgroquimico.getTipo() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El tipo de movimiento es obligatorio.");
        if (movimientoAgroquimico.getCantidad() == null || movimientoAgroquimico.getCantidad() <= 0)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "La cantidad del movimiento debe ser mayor que cero.");
        if (movimientoAgroquimico.getFecha() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La fecha del movimiento es obligatoria.");

        Agroquimico agroquimico = agroquimicoRepository.findById(movimientoAgroquimico.getAgroquimico().getId())
                .orElseThrow(() -> new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el agroquímico con ID " + movimientoAgroquimico.getAgroquimico().getId() + "."));

        double stockActual = agroquimico.getStockActual() != null ? agroquimico.getStockActual() : 0;
        if (movimientoAgroquimico.getTipo() == TipoMovimiento.ENTRADA) {
            agroquimico.setStockActual(stockActual + movimientoAgroquimico.getCantidad());
        } else {
            if (stockActual < movimientoAgroquimico.getCantidad())
                throw new BusinessValidationException(ErrorCodes.INSUFFICIENT_STOCK, "No hay suficiente stock de \"" + agroquimico.getNombre() + "\" para registrar esta salida.");
            agroquimico.setStockActual(stockActual - movimientoAgroquimico.getCantidad());
        }
        agroquimicoRepository.save(agroquimico);
        movimientoAgroquimico.setAgroquimico(agroquimico);

        return movimientoAgroquimicoRepository.save(movimientoAgroquimico);
    }

    @Override
    @Transactional
    public MovimientoAgroquimico delete(Long id) {
        MovimientoAgroquimico movimientoDb = findById(id);
        if (movimientoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el movimiento con ID " + id + ".");

        Agroquimico agroquimico = movimientoDb.getAgroquimico();
        double stockActual = agroquimico.getStockActual() != null ? agroquimico.getStockActual() : 0;
        if (movimientoDb.getTipo() == TipoMovimiento.ENTRADA) {
            if (stockActual < movimientoDb.getCantidad())
                throw new BusinessValidationException(ErrorCodes.INSUFFICIENT_STOCK, "No se puede eliminar esta entrada porque el stock actual de \"" + agroquimico.getNombre() + "\" ya fue consumido.");
            agroquimico.setStockActual(stockActual - movimientoDb.getCantidad());
        } else {
            agroquimico.setStockActual(stockActual + movimientoDb.getCantidad());
        }
        agroquimicoRepository.save(agroquimico);

        movimientoAgroquimicoRepository.delete(movimientoDb);
        return movimientoDb;
    }
}
