package com.nimsoc.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nimsoc.ticket.model.Ticket;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;

@Service
@AllArgsConstructor
public class TicketService {

    private final VectorStore vectorStore;

    public void parseAndSaveIncidenteJson(String filePath, int pageSize) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Register JSR-310 module for LocalDateTime
        try {
            List<Ticket> tickets = objectMapper.readValue(new File(filePath), new TypeReference<List<Ticket>>() {
            });

            List<Document> documents = tickets
                    .stream()
                    .map(ticket -> {
                        Map<String, Object> metadata = Map.of("summary", ticket.getSummary());
                        return new Document(ticket.getDescription(), metadata);
                    })
                    .toList();
            vectorStore.add(documents);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse incidente.json", e);
        }
    }

    private static final int MAX_RESULTS = 3;

    public List<Document> similaritySearch(String text) {
        SearchRequest searchRequest = SearchRequest
                .builder()
                .query(text)
                .topK(MAX_RESULTS)
                .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        return documents;
    }
}