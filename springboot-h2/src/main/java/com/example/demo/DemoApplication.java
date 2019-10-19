package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class DemoApplication {

	@Autowired
	LiquibaseProperties properties;
	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		properties.setChangeLog("classpath:/db.changelog-master.xml");
		liquibase.setChangeLog(this.properties.getChangeLog());
		liquibase.setContexts(this.properties.getContexts());
		liquibase.setDataSource(this.dataSource);
		liquibase.setDefaultSchema(this.properties.getDefaultSchema());
		liquibase.setDropFirst(this.properties.isDropFirst());
		liquibase.setShouldRun(this.properties.isEnabled());
		return liquibase;
	}
	
}
