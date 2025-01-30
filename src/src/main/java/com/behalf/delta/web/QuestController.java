package com.behalf.delta.web;

import java.util.List;


import com.behalf.delta.entity.QuestSession;

import com.behalf.delta.service.QuestService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired; // Add this import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.repo.QuestRepository;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/quests")
public class QuestController {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestService questService;

    @GetMapping
    public List<QuestMetadata> getAllQuests() {
        return questRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestMetadata> getQuestById(@PathVariable Long id) {
        return questRepository.findById(id).map(Quest -> new ResponseEntity<>(Quest, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/fetch")
    public List<QuestMetadata> getAllQuestById() {
        return questRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<QuestMetadata> createQuest(@RequestBody @Valid QuestMetadata quest)
            throws ResponseStatusException, HttpMessageNotReadableException
    {
            var res = questService.placeOrder(quest);
            return ResponseEntity.ok(res);
    }

    @PostMapping("/agreement")
    public ResponseEntity<String> assignedQuest(@RequestBody @Valid QuestSession questAgreement) throws ResponseStatusException, ValidationException {
            questService.assignQuest(questAgreement);
            return ResponseEntity.ok("Success");
    }

    @PostMapping("/success/{questId}")
    public ResponseEntity<String> finishQuest(@PathVariable Long questId){
        questService.questSuccess(questId);
        return ResponseEntity.ok("Success");
    }
}
