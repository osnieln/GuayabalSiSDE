package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.ConfiguracionCorreo;
import cu.edu.unah.GuayabalSiSDE.repository.ConfiguracionCorreoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ConfiguracionCorreoDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class ConfiguracionCorreoService {

    @Autowired
    private ConfiguracionCorreoRepository repo;

    public ConfiguracionCorreo getConfig() {
        return repo.findById(1L).orElseGet(() -> {
            ConfiguracionCorreo def = new ConfiguracionCorreo();
            def.setId(1L);
            def.setHost("smtp.gmail.com");
            def.setPuerto(587);
            def.setUsuario("");
            def.setContrasena("");
            def.setRemitente("");
            def.setActivo(false);
            return def;
        });
    }

    public ConfiguracionCorreo saveConfig(ConfiguracionCorreoDTO dto) {
        ConfiguracionCorreo cfg = repo.findById(1L).orElse(new ConfiguracionCorreo());
        cfg.setId(1L);
        cfg.setHost(dto.getHost() != null ? dto.getHost().trim() : "");
        cfg.setPuerto(dto.getPuerto());
        cfg.setUsuario(dto.getUsuario() != null ? dto.getUsuario().trim() : "");
        if (dto.getContrasena() != null && !dto.getContrasena().equals("*****")) {
            cfg.setContrasena(dto.getContrasena());
        }
        cfg.setRemitente(dto.getRemitente() != null ? dto.getRemitente().trim() : "");
        cfg.setActivo(!cfg.getUsuario().isBlank());
        return repo.save(cfg);
    }

    public JavaMailSenderImpl buildMailSender(ConfiguracionCorreo cfg) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(cfg.getHost());
        sender.setPort(cfg.getPuerto());
        sender.setUsername(cfg.getUsuario());
        sender.setPassword(cfg.getContrasena());
        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "10000");
        return sender;
    }

    public void testConexion(ConfiguracionCorreoDTO dto) throws MessagingException {
        String pwd = dto.getContrasena();
        if ("*****".equals(pwd)) {
            pwd = repo.findById(1L).map(ConfiguracionCorreo::getContrasena).orElse("");
        }
        ConfiguracionCorreo cfg = new ConfiguracionCorreo();
        cfg.setHost(dto.getHost() != null ? dto.getHost().trim() : "");
        cfg.setPuerto(dto.getPuerto());
        cfg.setUsuario(dto.getUsuario() != null ? dto.getUsuario().trim() : "");
        cfg.setContrasena(pwd);
        buildMailSender(cfg).testConnection();
    }
}
