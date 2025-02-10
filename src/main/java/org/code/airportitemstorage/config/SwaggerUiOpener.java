package org.code.airportitemstorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;

@Component
public class SwaggerUiOpener implements CommandLineRunner {
    @Value("${spring.web.swaggerUrl}")
    private String swaggerUrl;

    @Override
    public void run(String... args) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(swaggerUrl));
        } else {
            System.out.println("Swagger UI is available at: " + swaggerUrl);
        }
    }
}

