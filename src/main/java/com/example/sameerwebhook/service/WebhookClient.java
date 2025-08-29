package com.example.sameerwebhook.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.sameerwebhook.cfg.AppProperties;
import com.example.sameerwebhook.dto.GenerateWebhookRequest;
import com.example.sameerwebhook.dto.GenerateWebhookResponse;

@Component
public class WebhookClient {
  private static final Logger log = LoggerFactory.getLogger(WebhookClient.class);
  private final RestTemplate http;

  public WebhookClient(RestTemplateBuilder builder) {
    this.http = builder
        .setConnectTimeout(Duration.ofSeconds(15))
        .setReadTimeout(Duration.ofSeconds(30))
        .build();
  }

  public GenerateWebhookResponse generateWebhook(AppProperties props) {
    String url = props.getBaseUrl() + props.getGeneratePath();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    GenerateWebhookRequest payload =
        new GenerateWebhookRequest(props.getName(), props.getRegNo(), props.getEmail());

    log.info("POST {}", url);
    ResponseEntity<GenerateWebhookResponse> resp =
        http.postForEntity(url, new HttpEntity<>(payload, headers), GenerateWebhookResponse.class);

    if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
      throw new IllegalStateException("Failed to generate webhook: " + resp.getStatusCode());
    }
    return resp.getBody();
  }

  public void submitFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", accessToken); 

    Map<String, String> body = Map.of("finalQuery", finalQuery);
    ResponseEntity<String> resp =
        http.postForEntity(webhookUrl, new HttpEntity<>(body, headers), String.class);

    if (!resp.getStatusCode().is2xxSuccessful()) {
      throw new IllegalStateException("Submit failed: " + resp.getStatusCode() + " -> " + resp.getBody());
    }
    log.info("Submitted final query successfully.");
  }
}
