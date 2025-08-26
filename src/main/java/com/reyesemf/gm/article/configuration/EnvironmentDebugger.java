package com.reyesemf.gm.article.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class EnvironmentDebugger {
    
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentDebugger.class);
    
    @Value("${DATABASE_URL:NOT_SET}")
    private String databaseUrl;
    
    @Value("${DB_USERNAME:NOT_SET}")
    private String dbUsername;
    
    @Value("${DB_PASSWORD:NOT_SET}")
    private String dbPassword;
    
    @PostConstruct
    public void debugEnvironment() {
        logger.info("=== ENVIRONMENT VARIABLES DEBUG ===");
        logger.info("DATABASE_URL: {}", databaseUrl.length() > 50 ? databaseUrl.substring(0, 50) + "..." : databaseUrl);
        logger.info("DB_USERNAME: {}", dbUsername);
        logger.info("DB_PASSWORD: {}", dbPassword.length() > 5 ? dbPassword.substring(0, 5) + "..." : dbPassword);
        logger.info("=== END DEBUG ===");
        
        // También imprimir variables de entorno del sistema
        logger.info("System DATABASE_URL: {}", System.getenv("DATABASE_URL") != null ? "SET" : "NOT_SET");
        logger.info("System DB_USERNAME: {}", System.getenv("DB_USERNAME") != null ? "SET" : "NOT_SET");
        logger.info("System DB_PASSWORD: {}", System.getenv("DB_PASSWORD") != null ? "SET" : "NOT_SET");
        logger.info("System SCOPE_SUFFIX: {}", System.getenv("SCOPE_SUFFIX"));
    }
}