package diploma.cloudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CloudapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudapiApplication.class, args);
	}

}
