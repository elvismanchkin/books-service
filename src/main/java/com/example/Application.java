package com.example;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.slf4j.MDC;

@OpenAPIDefinition(
        info = @Info(
                title = "Reactive Books Service",
                version = "0.1.0",
                description = "API for managing books in a reactive way",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(name = "API Support", email = "support@example.com")
        )
)
public class Application {
    public static void main(String[] args) {
//        Micronaut.run(Application.class, args);
        MDC.put("application", "reactive-books-service");
        Micronaut.build(args)
                .mainClass(Application.class)
                .banner(false)
//                .defaultEnvironments("prod")
                .bootstrapEnvironment(true)
                .deduceEnvironment(true)
                .start();
    }
}