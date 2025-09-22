package site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcurrencyVisualizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrencyVisualizerApplication.class, args);

		// configurable data

			// total ticket amount
			// vendorCount
			// consumerCount
			// cap per consumer
			// ticket release rate
			// ticket retrieval rate
	}

}
