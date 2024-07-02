package com.example.demoaiollama.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SimpleAiController {

    private final ChatClient chatClient;

    @Autowired
    public SimpleAiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {

        String answer = chatClient.prompt().user(message).call().content();

        var map = new HashMap<String, String>();
        map.put("question", message);
        map.put("answer", answer);

        return map;

    }

    @GetMapping("/ai/generate/joke/{topic}")
    public Map<String, String> generateJoke(@PathVariable String topic) {

        String message = String.format("Tell me a joke about %s", topic);
        String answer = chatClient.prompt().user(message).call().content();

        var map = new HashMap<String, String>();
        map.put("question", message);
        map.put("answer", answer);

        return map;

    }
}
