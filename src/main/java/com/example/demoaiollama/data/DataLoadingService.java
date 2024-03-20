package com.example.demoaiollama.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DataLoadingService {

    private final VectorStore vectorStore;

    public DataLoadingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }


    public void load() {

        // Get data and Extract text from page html
        var dataUrl = "https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html";
        var tikaReader = new TikaDocumentReader(dataUrl);
        var extractDocs = tikaReader.get();

        // Split text into chunks
        var tokenTextSplitter = new TokenTextSplitter();
        var chunks = tokenTextSplitter.split(extractDocs.get(0).getContent(), 1536);
        log.info("CHUNKS = " + chunks.size());

        // Create Document for each text chunk
        var documents = chunks.stream()
                .map(d -> new Document(d, Map.of("name", "etl_pipeline")))
                .toList();
        log.info("Documents size:" + documents.size());

        // Create embedding for each document and store to vector database
        vectorStore.accept(documents);

    }
}
