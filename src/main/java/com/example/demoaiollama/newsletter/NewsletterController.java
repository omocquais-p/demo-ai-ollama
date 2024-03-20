package com.example.demoaiollama.newsletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class NewsletterController {

    @Value("classpath:/prompt/system-chatbot.st")
    private Resource systemPromptRes;

    @Value("classpath:/prompt/newsletter-prompt.srt")
    private Resource newsletterPromptRes;

    private final ChatClient chatClient;
    public NewsletterController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/newsletter/generate/{topic}")
    public Map<String, String> generateNewsletter(@PathVariable String topic) {
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
