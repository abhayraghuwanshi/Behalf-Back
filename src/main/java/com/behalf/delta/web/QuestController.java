package com.behalf.delta.web;

import java.util.List;
import com.behalf.delta.entity.QuestSession;
import com.behalf.delta.entity.dto.QuestMetadataDTO;
import com.behalf.delta.service.QuestRecommendationService;
import com.behalf.delta.service.QuestService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired; // Add this import

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import com.behalf.delta.entity.QuestMetadata;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestController {

    private final QuestService questService;

    private final QuestRecommendationService recommendationService;



    @GetMapping("/detail")
    public QuestMetadataDTO getAllQuests( @RequestParam("postId") Integer questId) {
        return questService.fetchASingleQuest(questId);

    }

    @GetMapping("/fetch")
    public List<QuestMetadataDTO> getAllQuests(@RequestParam(required = false, defaultValue = "India") String userCountry) {
        return questService.fetchAllQuests(userCountry);
    }


    @GetMapping("/recommend")
    public ResponseEntity<List<QuestMetadata>> getRecommendedQuests(
            @RequestParam("questId") Long questId,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "true") boolean includeSameCreator) {

        List<QuestMetadata> similarQuests = recommendationService.getSimilarQuests(questId, limit, includeSameCreator);
        return ResponseEntity.ok(similarQuests);
    }

    @PostMapping("/create")
    public QuestMetadataDTO createQuest(@RequestBody @Valid QuestMetadata quest) throws ResponseStatusException, HttpMessageNotReadableException {
        return questService.createQuest(quest);
    }

    @PostMapping("/agreement")
    public ResponseEntity<String> assignedQuest(@RequestBody @Valid QuestSession questAgreement) throws ResponseStatusException, ValidationException {
            questService.assignQuest(questAgreement);
            return ResponseEntity.ok("Success");
    }

    @PostMapping("/update/{questSessionId}")
    public ResponseEntity<String> updateStatus(@PathVariable Long questSessionId,  @RequestBody QuestSession questSession){
        String status = questService.updateQuest(questSessionId, questSession);
        return ResponseEntity.ok(status);
    }
}
