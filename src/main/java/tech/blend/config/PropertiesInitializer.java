package tech.blend.config;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;

import java.util.List;

@Slf4j
public class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final List<String> externalPropFiles = ImmutableList.of(
            "postgreSql.properties",
            "security.properties"
    );

    private static final List<String> classpathPropFiles = ImmutableList.of(
    );


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String propertyFilesPath = environment.getProperty("props.file.path");
        final MutablePropertySources mutablePropertySources = environment.getPropertySources();
        for (String propFile : classpathPropFiles) {
            try {
                mutablePropertySources.addLast(new ResourcePropertySource(propFile));
            } catch (Exception e) {
                log.error("Error reading property files {}", propFile, e);
            }
        }
        for (String propFile : externalPropFiles) {
            try {
                mutablePropertySources
                        .addLast(new ResourcePropertySource("file:" + propertyFilesPath + "/" + propFile));
            } catch (Exception e) {
                log.error("Error reading property files from {}", propertyFilesPath, e);
            }
        }
    }
}
