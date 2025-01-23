package com.behalf.delta.web;

import java.util.List;
import java.util.Optional;

import com.behalf.delta.entity.QuestAgreement;
import com.behalf.delta.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired; // Add this import
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/create")
    public ResponseEntity<QuestMetadata> createQuest(@RequestBody QuestMetadata quest) {
        try {
            var res = questService.placeOrder(quest);
            return ResponseEntity.ok(res);
        } catch (ResponseStatusException e) {
            // Use the custom HTTP status and error message
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            // Fallback for unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/agreement")
    public ResponseEntity<String> assignedQuest(@RequestBody QuestAgreement questAgreement) {
        try {
            questService.assignQuest(questAgreement);
            return ResponseEntity.ok("Success");
        } catch (ResponseStatusException e) {
            // Use the custom HTTP status and error message
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            // Fallback for unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/success/{questId}")
    public ResponseEntity<String> finishQuest(@PathVariable Long questId){
        questService.questSuccess(questId);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        return questRepository.findById(id).map(Quest -> {
            questRepository.delete(Quest);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
