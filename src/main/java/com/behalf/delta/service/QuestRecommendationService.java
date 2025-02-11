package com.behalf.delta.service;


import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.repo.QuestRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class QuestRecommendationService {

    @Autowired
    private QuestRepository questRepository;

    /**
     * Find similar quests based on multiple factors:
     * 1. Same creator (optional)
     * 2. Similar reward range
     * 3. Similar validity period
     * 4. Similar creation time
     * 5. Same status
     *
     * @param questId The ID of the quest to find similar quests for
     * @param limit Maximum number of similar quests to return
     * @param includeSameCreator Whether to include quests from the same creator
     * @return List of similar quests
     */
    public List<QuestMetadata> getSimilarQuests(Long questId, int limit, boolean includeSameCreator) {
        // Find the source quest
        Optional<QuestMetadata> sourceQuestOpt = questRepository.findById(questId);
        if (!sourceQuestOpt.isPresent()) {
            throw new RuntimeException("Quest not found with id: " + questId);
        }

        QuestMetadata sourceQuest = sourceQuestOpt.get();
        Long creatorId = sourceQuest.getQuestCreatorId();
        Integer reward = sourceQuest.getQuestReward();
        Date validity = sourceQuest.getQuestValidity();
        String status = sourceQuest.getQuestStatus();
        Date creationDate = sourceQuest.getCreationTimestamp();

        // Calculate reward range (±30%)
        int minReward = reward != null ? (int) (reward * 0.7) : 0;
        int maxReward = reward != null ? (int) (reward * 1.3) : Integer.MAX_VALUE;

        // Find similar quests using custom repository method
        List<QuestMetadata> similarQuests;

        if (includeSameCreator) {
            similarQuests = questRepository.findSimilarQuests(
                    questId, minReward, maxReward, status,
                    PageRequest.of(0, limit * 2)  // Fetch more than needed for filtering
            );
        } else {
            similarQuests = questRepository.findSimilarQuestsExcludingCreator(
                    questId, creatorId, minReward, maxReward, status,
                    PageRequest.of(0, limit * 2)  // Fetch more than needed for filtering
            );
        }

        // Further refine results by scoring each quest based on multiple factors
        return similarQuests.stream()
                .map(quest -> {
                    double score = calculateSimilarityScore(sourceQuest, quest);
                    return new ScoredQuest(quest, score);
                })
                .sorted((a, b) -> Double.compare(b.score(), a.score())) // Sort by score descending
                .limit(limit)
                .map(ScoredQuest::quest)
                .collect(Collectors.toList());
    }

    /**
     * Calculate a similarity score between two quests
     * Higher score means more similar
     */
    private double calculateSimilarityScore(QuestMetadata source, QuestMetadata target) {
        double score = 0.0;

        // Factor 1: Reward similarity (up to 30 points)
        if (source.getQuestReward() != null && target.getQuestReward() != null) {
            double rewardRatio = Math.min(source.getQuestReward(), target.getQuestReward()) /
                    (double) Math.max(source.getQuestReward(), target.getQuestReward());
            score += rewardRatio * 30;
        }

        // Factor 2: Validity date similarity (up to 25 points)
        if (source.getQuestValidity() != null && target.getQuestValidity() != null) {
            long diffInMillies = Math.abs(source.getQuestValidity().getTime() - target.getQuestValidity().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // If within 30 days, give points inversely proportional to difference
            if (diffInDays <= 30) {
                score += 25 * (1 - (diffInDays / 30.0));
            }
        }

        // Factor 3: Creation date similarity (up to 15 points)
        if (source.getCreationTimestamp() != null && target.getCreationTimestamp() != null) {
            long diffInMillies = Math.abs(source.getCreationTimestamp().getTime() - target.getCreationTimestamp().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // If created within 60 days of each other, give points
            if (diffInDays <= 60) {
                score += 15 * (1 - (diffInDays / 60.0));
            }
        }

        // Factor 4: Same status (10 points)
        if (source.getQuestStatus() != null && source.getQuestStatus().equals(target.getQuestStatus())) {
            score += 10;
        }

        // Factor 5: Text similarity in instructions (up to 20 points)
        if (source.getQuestInstructions() != null && target.getQuestInstructions() != null) {
            double textSimilarity = calculateTextSimilarity(
                    source.getQuestInstructions(),
                    target.getQuestInstructions()
            );
            score += textSimilarity * 20;
        }

        return score;
    }

    /**
     * Calculate text similarity using Jaccard similarity coefficient
     * (intersection over union of words)
     */
    private double calculateTextSimilarity(String text1, String text2) {
        // Convert to lowercase and split by non-word characters
        String[] words1 = text1.toLowerCase().split("\\W+");
        String[] words2 = text2.toLowerCase().split("\\W+");

        // Create sets of words
        java.util.Set<String> set1 = new java.util.HashSet<>(java.util.Arrays.asList(words1));
        java.util.Set<String> set2 = new java.util.HashSet<>(java.util.Arrays.asList(words2));

        // Calculate intersection size
        java.util.Set<String> intersection = new java.util.HashSet<>(set1);
        intersection.retainAll(set2);

        // Calculate union size
        java.util.Set<String> union = new java.util.HashSet<>(set1);
        union.addAll(set2);

        // Return Jaccard similarity
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    // Helper class to store a quest with its similarity score

        private record ScoredQuest(QuestMetadata quest, double score) {

    }
}
