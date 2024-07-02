package com.example.demoaiollama.newsletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class NewsletterController {

    @Value("classpath:/prompt/system-chatbot.st")
    private Resource systemPromptRes;

    @Value("classpath:/prompt/newsletter-prompt.srt")
    private Resource newsletterPromptRes;

    private final ChatClient chatClient;

    public NewsletterController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/newsletter/generate/{topic}")
    public Map<String, String> generateNewsletter(@PathVariable String topic) {

        return Map.of("generation", chatClient.prompt()
                .user(userSpec -> userSpec.text(newsletterPromptRes).param("topics", String.join(", ", topic)) )
                .system(systemSpec -> systemSpec.text(systemPromptRes))
                .call()
                .content());
    }
}
