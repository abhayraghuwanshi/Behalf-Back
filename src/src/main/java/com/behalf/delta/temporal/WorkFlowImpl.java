package com.behalf.delta.temporal;

import java.time.Duration;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkFlowImpl implements WorkFlow {
    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1)).setMaximumInterval(Duration.ofSeconds(100))
            .setBackoffCoefficient(2).setMaximumAttempts(50000).build();

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30)).setRetryOptions(retryoptions).build();

    private final Activity activity = Workflow.newActivityStub(Activity.class, options);

    public boolean isQuestHunterAssigned = false;

    public boolean isPaymentDone = false;

    public boolean isQuestSuccess = false;
    @Override
    public void startWorkflow() {
        // TODO Auto-generated method stub
        log.info("running workflow");
        activity.createQuest();

        Workflow.await(()-> isQuestHunterAssigned);
//        Workflow.await(()-> isPaymentDone);
        Workflow.await(()-> isQuestSuccess);

        activity.questReward();

        log.info("running workflow: finished");

    }

    @Override
    public void signalQuestHunterAssigned() {
        this.isQuestHunterAssigned = true;

    }

    @Override
    public void signalPaymentDone() {
        this.isPaymentDone = true;
    }

    @Override
    public void signalQuestSuccess() {
        this.isQuestSuccess = true;
    }

}
