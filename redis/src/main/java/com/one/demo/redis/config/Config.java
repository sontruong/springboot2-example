package com.one.demo.redis.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Config implements WebMvcConfigurer {
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/WEB-INF/", "classpath:/static/",
			"classpath:/public/" };

	/*
	 * swagger configuration
	 */
	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**")).build().pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(false).enableUrlTemplating(false).apiInfo(getInfo())
				.tags(new Tag("Redis demo", "Redis demo"));
	}

	@SuppressWarnings("rawtypes")
	private ApiInfo getInfo() {
		Collection<VendorExtension> collection = new ArrayList<>();
		return new ApiInfo("Redis demo", "All API relatings OOnes SSO Service", "1.0", "http://oones.net",
				new Contact("OOnes team", "http://oones.net", "support@oones.net"), "MIT",
				"http://oones.net/license", collection);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/images/**.png").addResourceLocations("/static/", "classpath:/static/");
		registry.addResourceHandler("/images/**.ico").addResourceLocations("/static/", "classpath:/static/");
		registry.addResourceHandler("/style/**.css").addResourceLocations("/static/styles/",
				"classpath:/static/styles/");
		registry.addResourceHandler("/scripts/**.js").addResourceLocations("/static/js/", "classpath:/static/js/");
		registry.addResourceHandler("/page/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);

	}
}
