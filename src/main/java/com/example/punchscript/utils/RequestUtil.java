package com.example.punchscript.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequestUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void printMessage(HttpResponse<String> response, String node) throws JsonProcessingException {
        String body = response.getBody();
        JsonNode jsonNode = objectMapper.readTree(body);
        String message = jsonNode.path(node).asText();
        log.info(message);
    }
}
