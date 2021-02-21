package com.one.demo.redis.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

@Configuration
public class RedisConfiguration {

   @Value("${datasource.redis.hostname}")
   private String redisHost;

   @Value("${datasource.redis.port}")
   private Integer redisPort;

   @Value("${datasource.redis.password}")
   private String password;

   @Bean
   JedisConnectionFactory jedisConnectionFactory() {
          RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost,
                       redisPort);
          if (!StringUtils.isEmpty(password)) {
             redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
          }
          return new JedisConnectionFactory(redisStandaloneConfiguration);
   }

   @Bean
   public RedisTemplate<String, Object> redisTemplate() {
          RedisTemplate<String, Object> template = new RedisTemplate<>();
          template.setConnectionFactory(jedisConnectionFactory());
          return template;
   }
}