package com.reyesemf.gm.article.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentDebugger implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentDebugger.class);
    
    @Value("${DATABASE_URL:NOT_SET}")
    private String databaseUrl;
    
    @Value("${DB_USERNAME:NOT_SET}")
    private String dbUsername;
    
    @Value("${DB_PASSWORD:NOT_SET}")
    private String dbPassword;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("=== ENVIRONMENT VARIABLES DEBUG ===");
        logger.info("DATABASE_URL: {}", databaseUrl.length() > 50 ? databaseUrl.substring(0, 50) + "..." : databaseUrl);
        logger.info("DB_USERNAME: {}", dbUsername);
        logger.info("DB_PASSWORD: {}", dbPassword.length() > 5 ? dbPassword.substring(0, 5) + "..." : dbPassword);
        logger.info("=== END DEBUG ===");
    }
}