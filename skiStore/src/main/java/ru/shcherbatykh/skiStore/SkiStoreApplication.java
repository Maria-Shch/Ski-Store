package ru.shcherbatykh.skiStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Configuration
@EnableJpaRepositories("ru.shcherbatykh.skiStore.repositories")
@EnableTransactionManagement
@EntityScan("ru.shcherbatykh.skiStore.models")
@ComponentScan(basePackages = "ru.shcherbatykh")
public class SkiStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkiStoreApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localResolver = new SessionLocaleResolver();
        localResolver.setDefaultLocale(new Locale("ru"));
        return localResolver;
    }

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("validation");
        return messageSource;
    }

}

//todo sort model types before sending to front
//todo at least one inventory checked for go to payment
//todo able to change username (email)