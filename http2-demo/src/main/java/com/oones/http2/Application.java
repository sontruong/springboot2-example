/**
 * 
 */
package com.oones.http2;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author son.truong
 * Aug 6, 2019, 7:13:46 PM
 */
@Controller
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

   public static void main(String[] args) {
      SpringApplication app = new SpringApplication(Application.class);
      app.run(args);
   }
   
   @RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String apps() throws IOException {
		return "Http/2 demo";
	}
}
