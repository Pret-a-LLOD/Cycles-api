package sid.cycles.cycles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import sid.cycles.cycles.CyclesApplication;

/**
 * @author ock
 */
@SpringBootApplication
public class CyclesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyclesApplication.class, args);
    }
    
    @Bean
    public OpenAPI customOpenAPI() {
    
        return new OpenAPI()
          .info(new Info()
	          .title("Cycle-based Translation Inference Service API")
	          .version("1.0.0-oas3")
	          .description("This API allows to obtain translations from a source language into a target language by exploring the connections in a graph of dictionaries. "
	          		+ "This technique was proposed by [1] in 2006. "
	          		+ "The idea was exploting the properties of the Apertium RDF Graph by using cycles to identify potential targets that may be a translation of a given word. "
	          		+ "[1] Villegas, M., Melero, M., Bel, N., & Gracia, J. (2016, May). Leveraging RDF graphs for crossing multiple bilingual dictionaries. In Proceedings of the Tenth International Conference on Language Resources and Evaluation (LREC'16) (pp. 868-876)")
	      );
    }


}
