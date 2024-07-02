package com.example.demoaiollama.movie;

import com.example.demoaiollama.dto.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MovieController {
    private final ChatClient chatClient;

    @Value("classpath:/prompt/system-chatbot.st")
    private Resource systemPromptRes;

    @Value("classpath:/prompt/movie-prompt.srt")
    private Resource moviePromptRes;

    public MovieController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/movie/generate")
    public Movie generateMovies(@RequestParam String category, @RequestParam String year) {

        String content = chatClient.prompt()
                .user(p -> p.text(moviePromptRes).param("category", category).param("year", year))
                .system(systemSpec -> systemSpec.text(systemPromptRes))
                .call()
                .content();

        return new Movie(category, Integer.parseInt(year), content);
    }
}
