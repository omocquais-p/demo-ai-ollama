package com.example.demoaiollama.movie;

import com.example.demoaiollama.dto.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MovieController {
    private final ChatClient chatClient;

    @Value("classpath:/prompt/system-chatbot.st")
    private Resource systemPromptRes;

    @Value("classpath:/prompt/movie-prompt.srt")
    private Resource moviePromptRes;

    public MovieController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/movie/generate")
    public Movie generateMovies(@RequestParam String category, @RequestParam String year) {
        final var sysMsg = new SystemPromptTemplate(systemPromptRes).createMessage();
        log.info("Created system prompt: {}", sysMsg);

        final var movieMsg = new PromptTemplate(moviePromptRes).
                createMessage(Map.of(
                                "category", category,
                                "year", year
                        )
                );
        log.info("Created movie prompt: {}", movieMsg);

        final var prompt = new Prompt(List.of(sysMsg, movieMsg));
        String content = chatClient.call(prompt).getResult().getOutput().getContent();
        return new Movie(category, Integer.parseInt(year), content);
    }
}
