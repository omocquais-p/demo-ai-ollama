package com.example.demoaiollama.qa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/qa")
public class QAController {
    private final QAService qaService;

    @Autowired
    public QAController(QAService qaService) {
        this.qaService = qaService;
    }

    @GetMapping
    public Map<String, String> completion(@RequestParam(value = "question", defaultValue = "What is ETL pipeline?") String question,
                          @RequestParam(value = "stuffit", defaultValue = "true") boolean stuffit) {

        String answer = this.qaService.generate(question, stuffit);

        var map = new HashMap<String, String>();
        map.put("question", question);
        map.put("answer", answer);

        return map;
    }
}
