package com.example.demoaiollama.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DataLoadingService {

    private final VectorStore vectorStore;

    @Value("classpath:/documents/FAQ-SpringAcademy.pdf")
    private Resource pdfDocument;

    public DataLoadingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }


    public void load() {
        // Get data and Extract text from page html
        var dataUrl = "https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html";
        saveDocument(new TikaDocumentReader(dataUrl), "etl_pipeline");
        saveDocument(new TikaDocumentReader(pdfDocument), "spring_academy");
    }

    private void saveDocument(TikaDocumentReader tikaReader, String documentName){
        // Split text into chunks
        //        var chunks = tokenTextSplitter.split(new Document(extractDocs.getFirst().getContent()));
//        log.info("CHUNKS = " + chunks.size());
//
//        // Create Document for each text chunk
//
//        var documents = chunks.stream()
//                .map(d -> new Document(d, Map.of("name", documentName)))
//                .toList();
//        log.info("Documents size:" + documents.size());

        // Create embedding for each document and store to vector database
        vectorStore.accept(new TokenTextSplitter().split(new Document(tikaReader.get().getFirst().getContent())));
    }
}
