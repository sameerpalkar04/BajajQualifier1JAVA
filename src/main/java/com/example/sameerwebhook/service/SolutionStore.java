package com.example.sameerwebhook.service;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SolutionStore {
  private final ObjectMapper om = new ObjectMapper();

  public void save(String regNo, String finalQuery, String webhookUrl, String accessToken) throws Exception {
    Map<String, Object> doc = new HashMap<>();
    doc.put("regNo", regNo);
    doc.put("finalQuery", finalQuery);
    doc.put("webhookUrl", webhookUrl);
    doc.put("accessToken", accessToken);
    doc.put("savedAt", Instant.now().toString());

    File out = new File("target/solution.json");
    Files.createDirectories(out.getParentFile().toPath());
    om.writerWithDefaultPrettyPrinter().writeValue(out, doc);
  }
}
