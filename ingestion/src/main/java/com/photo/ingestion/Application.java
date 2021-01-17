package com.photo.ingestion;

import com.photo.ingestion.config.ServiceConfig;
import com.photo.ingestion.security.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication(scanBasePackages = "com.stix.ingestion")
@EnableResourceServer
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private ServiceConfig serviceConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
        jedisConnFactory.setHostName(serviceConfig.getRedisServer());
        jedisConnFactory.setPort(serviceConfig.getRedisPort());
        return jedisConnFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    ;

    @Bean
    public OAuth2ClientContext oAuth2ClientContext() {
        return new Context();
    }

    @Bean
    public OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        OAuth2ProtectedResourceDetails details = new BaseOAuth2ProtectedResourceDetails();
        ((BaseOAuth2ProtectedResourceDetails) details).setTokenName("apiKey");
        return details;
    }
}
