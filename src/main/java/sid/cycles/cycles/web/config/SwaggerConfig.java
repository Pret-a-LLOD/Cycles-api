package sid.cycles.cycles.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2

public interface SwaggerConfig {

    default ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Joins Multi Project")
                .description("API documents")
                .build();
    }

    @Bean
    public Docket apiDocket();
}


