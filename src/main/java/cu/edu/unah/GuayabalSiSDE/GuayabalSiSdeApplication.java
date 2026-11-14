package cu.edu.unah.GuayabalSiSDE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GuayabalSiSdeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuayabalSiSdeApplication.class, args);
	}

}
