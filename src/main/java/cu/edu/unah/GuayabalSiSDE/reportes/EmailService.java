package cu.edu.unah.GuayabalSiSDE.reportes;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    public void enviarExcel(String destinatario, String asunto,
                             byte[] excelBytes, String nombreArchivo) throws MessagingException {
        if (mailSender == null) {
            throw new IllegalStateException(
                    "El servidor de correo no está configurado. "
                    + "Configure las variables de entorno MAIL_HOST, MAIL_USERNAME y MAIL_PASSWORD.");
        }
        String remitente = (from == null || from.isBlank()) ? "noreply@guayabal.cu" : from;

        MimeMessage msg = mailSender.createMimeMessage();
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
        mailSender.send(msg);
    }
}
