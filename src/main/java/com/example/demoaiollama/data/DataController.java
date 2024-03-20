package com.example.demoaiollama.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

    private final JdbcTemplate jdbcTemplate;

    private final DataLoadingService dataLoadingService;

    public DataController(JdbcTemplate jdbcTemplate, DataLoadingService dataLoadingService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataLoadingService = dataLoadingService;
    }


    @PostMapping("/load")
    public ResponseEntity<String> load() {
        try {
            this.dataLoadingService.load();
            return ResponseEntity.ok("Data loaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while loading data: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vector_store", Integer.class);
    }

    @PostMapping("/delete")
    public void delete() {
        jdbcTemplate.update("DELETE FROM vector_store");
    }

}
