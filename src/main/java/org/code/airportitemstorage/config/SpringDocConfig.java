package org.code.airportitemstorage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
//    http://localhost:8080/swagger-ui/index.html
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("机场暂存物品系统Api")
                        .description("机场暂存物品系统")
                        .version("v1.0.0"));
    }
}
