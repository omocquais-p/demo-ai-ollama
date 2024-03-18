package com.example.demoaiollama;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class SimpleAiController {

    private final ChatClient chatClient;

    @Value("classpath:/prompt/system-prompt.srt")
    private Resource systemPromptRes;

    @Value("classpath:/prompt/newsletter-prompt.srt")
    private Resource newsletterPromptRes;

    @Autowired
    public SimpleAiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("ai/generate/joke/{topic}")
    public Map<String, String>  generateJoke(@PathVariable String topic) {
        return Map.of("generation", chatClient.call(String.format("Tell me a joke about %s", topic)));
    }

    @GetMapping("ai/generate/prompt/{topic}")
    public Map<String, String>  generateWithPrompt(@PathVariable String topic) {
        final var sysMsg = new SystemPromptTemplate(systemPromptRes).createMessage();
        log.info("Created system prompt: {}", sysMsg);

        final var newsletterMsg = new PromptTemplate(newsletterPromptRes).
                createMessage(Map.of(
                        "topics", String.join(", ", topic))
                );
        log.info("Created newsletter prompt: {}", newsletterMsg);

        final var prompt = new Prompt(List.of(sysMsg, newsletterMsg));


        return Map.of("generation", chatClient.call(prompt).getResult().getOutput().getContent());
    }
}
