package com.reyesemf.gm.article.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EnvironmentDebugger {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentDebugger.class);

    @Autowired
    private Environment environment;

    @PostConstruct
    public void debugEnvironment() {
        logger.info("=== ENVIRONMENT DEBUG ===");
        logger.info("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));
        
        // Debug database variables
        String databaseUrl = environment.getProperty("DATABASE_URL");
        String dbUsername = environment.getProperty("DB_USERNAME");
        String dbPassword = environment.getProperty("DB_PASSWORD");
        
        logger.info("DATABASE_URL present: {}", databaseUrl != null);
        logger.info("DB_USERNAME present: {}", dbUsername != null);
        logger.info("DB_PASSWORD present: {}", dbPassword != null);
        
        if (databaseUrl != null) {
            logger.info("DATABASE_URL starts with: {}", databaseUrl.substring(0, Math.min(20, databaseUrl.length())));
        }
        if (dbUsername != null) {
            logger.info("DB_USERNAME: {}", dbUsername);
        }
        if (dbPassword != null) {
            logger.info("DB_PASSWORD starts with: {}", dbPassword.substring(0, Math.min(5, dbPassword.length())));
        }
        
        // Debug all environment variables that start with DB or DATABASE
        logger.info("=== ALL DB RELATED ENV VARS ===");
        System.getenv().entrySet().stream()
            .filter(entry -> entry.getKey().startsWith("DB") || entry.getKey().startsWith("DATABASE"))
            .forEach(entry -> logger.info("ENV: {} = {}", entry.getKey(), 
                entry.getValue() != null ? entry.getValue().substring(0, Math.min(20, entry.getValue().length())) + "..." : "null"));
        
        logger.info("=== END ENVIRONMENT DEBUG ===");
    }
}