/**
 * 
 */
package com.oones.cdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author son.truong
 */
@Configuration
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

   public static void main(String[] args) {
      SpringApplication app = new SpringApplication(Application.class);
      app.run(args);
   }
}
