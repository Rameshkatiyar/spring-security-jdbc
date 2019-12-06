package tech.blend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import tech.blend.config.PropertiesInitializer;

@SpringBootApplication
@ComponentScan(value = "tech.blend", lazyInit = true)
public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new PropertiesInitializer())
                .run(args);
    }
}
