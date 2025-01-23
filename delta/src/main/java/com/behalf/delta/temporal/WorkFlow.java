package com.behalf.delta.temporal;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WorkFlow {


    public static final String QUEUE_NAME = "Customer_Order";
    @WorkflowMethod
    void startWorkflow();

    @SignalMethod
    void signalQuestHunterAssigned();

    @SignalMethod
    void signalPaymentDone();

    @SignalMethod
    void signalQuestSuccess();


}
