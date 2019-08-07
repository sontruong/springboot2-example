package com.example.demo.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Bean
	public Docket swaggerApi() {
		List<ResponseMessage> list = new java.util.ArrayList<>();
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**")).build().pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.POST, list)
				.globalResponseMessage(RequestMethod.PUT, list)
				.globalResponseMessage(RequestMethod.DELETE, list)
				.globalResponseMessage(RequestMethod.GET, list)
				.enableUrlTemplating(false).apiInfo(getInfo());
	}

	@SuppressWarnings("deprecation")
	private ApiInfo getInfo() {
		return new ApiInfo("Test", "Test", "Test", "Test", "Test", "Test", "Test");
	}
}
