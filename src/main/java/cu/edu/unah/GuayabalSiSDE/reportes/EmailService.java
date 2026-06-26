package cu.edu.unah.GuayabalSiSDE.reportes;

import cu.edu.unah.GuayabalSiSDE.entity.ConfiguracionCorreo;
import cu.edu.unah.GuayabalSiSDE.services.ConfiguracionCorreoService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private ConfiguracionCorreoService configService;

    public void enviarExcel(String destinatario, String asunto,
                             byte[] excelBytes, String nombreArchivo) throws MessagingException {
        ConfiguracionCorreo cfg = configService.getConfig();
        if (cfg.getUsuario() == null || cfg.getUsuario().isBlank()) {
            throw new IllegalStateException(
                    "Correo no configurado. Vaya a Administración → Configuración de Correo.");
        }
        JavaMailSenderImpl sender = configService.buildMailSender(cfg);
        String remitente = (cfg.getRemitente() != null && !cfg.getRemitente().isBlank())
                ? cfg.getRemitente() : cfg.getUsuario();

        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setFrom(remitente);
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(
                "<p>Se adjunta el reporte <b>" + asunto + "</b> en formato Excel (.xlsx).</p>"
                + "<p><small>Generado por el Sistema de Gestión Agrícola — Guayabal</small></p>",
                true);
        helper.addAttachment(
                nombreArchivo,
                new ByteArrayResource(excelBytes),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        sender.send(msg);
    }
}
