package com.behalf.delta.temporal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivityImpl implements Activity {

    @Override
    public void createQuest() {
       log.info("create quest");
    }


    @Override
    public void questReward() {
        log.info("reward quest");

    }

}
