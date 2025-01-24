package com.behalf.delta.service;

import com.behalf.delta.entity.QuestAgreement;
import com.behalf.delta.entity.QuestMetadata;
import com.behalf.delta.exception.DatabaseException;
import com.behalf.delta.exception.WorkflowException;
import com.behalf.delta.repo.QuestAgreementRepo;
import com.behalf.delta.repo.QuestRepository;
import com.behalf.delta.temporal.WorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class QuestService {

    @Autowired
    private WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestAgreementRepo questAgreementRepo;

    public QuestMetadata placeOrder(QuestMetadata quest) {
        try {
            // Step 1: Persist the Quest to the repository
            QuestMetadata savedQuest = saveQuestToDatabase(quest);
            long workflowId = savedQuest.getId();

            // Step 2: Start Temporal workflow
            startQuestWorkflow(workflowId);

            return savedQuest;
        } catch (DatabaseException e) {
            log.error("Database error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred");
        } catch (WorkflowException e) {
            log.error("Workflow error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Workflow service error occurred");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }


    // Helper method to save the Quest to the database
    private QuestMetadata saveQuestToDatabase(QuestMetadata quest) throws Exception {
        try {
            log.info("Saving quest to the database");
            return questRepository.saveAndFlush(quest);
        } catch (Exception e) {
            log.error("Failed to persist the quest in the repository", e);
            throw new DatabaseException("Database operation failed", e);
        }
    }

    // Helper method to start the Temporal workflow
    private void startQuestWorkflow(long workflowId) throws Exception {
        try {
            log.info("Starting workflow with ID: {}", workflowId);
            WorkFlow workflow = createWorkFlowConnection(workflowId);
            WorkflowClient.start(workflow::startWorkflow);
            log.info("Workflow with ID: {} started successfully", workflowId);
        } catch (Exception e) {
            log.error("Failed to start workflow with ID: {}", workflowId, e);
            throw new WorkflowException("Workflow initiation failed", e);
        }
    }

    public WorkFlow createWorkFlowConnection(Long id) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(WorkFlow.QUEUE_NAME)
                .setWorkflowId("Order_" + id)
                .build();

        return workflowClient.newWorkflowStub(WorkFlow.class, options);
    }

    public void assignQuest(QuestAgreement questAgreement){
        try {
            WorkFlow workflow = workflowClient.newWorkflowStub(WorkFlow.class, "Order_" + questAgreement.getQuestId());
            workflow.signalQuestHunterAssigned();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
        }
        try {
            questAgreementRepo.saveAndFlush(questAgreement);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Exception has occur");
        }

    }

    public void pay(Long workflowId){

        WorkFlow workflow = workflowClient.newWorkflowStub(WorkFlow.class, "Order_" + workflowId);
        workflow.signalPaymentDone();
    }

    public void questSuccess(Long workflowId){

        try {
            WorkFlow workflow = workflowClient.newWorkflowStub(WorkFlow.class, "Order_" + workflowId);
            workflow.signalQuestSuccess();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
        }
    }


}
