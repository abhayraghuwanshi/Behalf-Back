package com.behalf.delta.temporal;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface Activity {


    @ActivityMethod
    void createQuest(); // event created from frontend

    @ActivityMethod
    void questReward(); // transfer the money to questCreator or to the questHunter
}
