package com.example.sameerwebhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.sameerwebhook.cfg.AppProperties;
import com.example.sameerwebhook.dto.GenerateWebhookResponse;
import com.example.sameerwebhook.service.SQLSolu;
import com.example.sameerwebhook.service.SolutionStore;
import com.example.sameerwebhook.service.WebhookClient;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  CommandLineRunner runner(AppProperties props, WebhookClient client, SolutionStore store) {
    return args -> {
      GenerateWebhookResponse gw = client.generateWebhook(props);

      final String finalQuery = SQLSolu.Q1_POSTGRES;

      store.save(props.getRegNo(), finalQuery, gw.getWebhook(), gw.getAccessToken());

      client.submitFinalQuery(gw.getWebhook(), gw.getAccessToken(), finalQuery);

      log.info("Done: solution stored and submitted.");
    };
  }
}
