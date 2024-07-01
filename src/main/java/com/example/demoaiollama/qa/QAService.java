package com.example.demoaiollama.qa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QAService {
    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    @Value("classpath:/prompt/system-qa-documents.srt")
    private Resource qaSystemPromptResource;

    @Value("classpath:/prompt/system-chatbot.st")
    private Resource chatBotSystemPromptResource;

    public QAService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public String generate(String message, boolean stuffit) {

        Message systemMessage = getSystemMessage(message, stuffit);
        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // A single line API call to connect to your favorite LLM and get a response.
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
    }

    private Message getSystemMessage(String question, boolean stuffit) {
        if (stuffit) {

            log.info("Retrieving relevant documents");

            // Retrieve similar chunks from the vector database
            List<Document> similarDocuments = vectorStore.similaritySearch(
                    SearchRequest.query("")
                            .withQuery(question) // Text to use for embedding similarity comparison.
                            .withSimilarityThreshold(0.1) // Similarity threshold score to filter the search response by.
                            .withTopK(3) // the top 'k' similar results to return.
            );

            log.info(String.format("Found %s relevant documents.", similarDocuments.size()));

            String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));

            // Combine context and question in a prompt
            SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(qaSystemPromptResource);
            return systemPromptTemplate.createMessage(Map.of("retrieved_chunk", documents, "question", question));

        } else {
            log.info("Not stuffing the prompt, using generic prompt");
            return new SystemPromptTemplate(chatBotSystemPromptResource).createMessage();
        }
    }
}
